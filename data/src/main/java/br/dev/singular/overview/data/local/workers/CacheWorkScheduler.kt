package br.dev.singular.overview.data.local.workers

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheWorkScheduler @Inject constructor(
    private val workManager: WorkManager
) {
    private val networkConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    fun schedule() {
        makeOneTime<DeleteSuggestionsCacheWorker>(uniqueWorkName = "delete_suggestions_cache_work")
        makeOneTime<DeleteCatalogCacheWorker>(uniqueWorkName = "delete_catalog_cache_work")
        makeOneTime<SaveGenreCacheWorker>(
            uniqueWorkName = "save_genre_cache_work",
            constraints = networkConstraints
        )
        makePeriodic<RefreshCatalogCacheWorker>(
            uniqueWorkName = "refresh_catalog_cache_work",
            repeatIntervalHours = 24L
        )
    }

    private inline fun <reified T : ListenableWorker> makeOneTime(
        uniqueWorkName: String,
        constraints: Constraints = Constraints.NONE
    ) {
        val workerRequest = OneTimeWorkRequestBuilder<T>()
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniqueWork(
            uniqueWorkName,
            ExistingWorkPolicy.KEEP,
            workerRequest
        )
    }

    private inline fun <reified T : ListenableWorker> makePeriodic(
        uniqueWorkName: String,
        repeatIntervalHours: Long
    ) {
        val workerRequest = PeriodicWorkRequestBuilder<T>(repeatIntervalHours, TimeUnit.HOURS)
            .setConstraints(networkConstraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName,
            ExistingPeriodicWorkPolicy.UPDATE,
            workerRequest
        )
    }
}
