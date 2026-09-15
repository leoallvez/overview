package br.dev.singular.overview.data.local.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

class WorkManagerFacade(
    private val _context: Context
) {
    fun init() {
        scheduleDeleteMediasCacheTask()
        scheduleDeleteSuggestionsCacheTask()
        scheduleDeleteCatalogCacheTask()
        scheduleSaveGenreCacheTask()
    }

    private fun scheduleDeleteMediasCacheTask() = makeOneTime<DeleteMediasCacheWorker>()

    private fun scheduleDeleteSuggestionsCacheTask() = makeOneTime<DeleteSuggestionsCacheWorker>()

    private fun scheduleDeleteCatalogCacheTask() = makeOneTime<DeleteCatalogCacheWorker>()

    private fun scheduleSaveGenreCacheTask() = makeOneTime<SaveGenreCacheWorker>()

    // Unique so a sync still waiting for the network is not enqueued again.
    fun syncFavorites() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workerRequest = OneTimeWorkRequestBuilder<SyncFavoritesWorker>()
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(_context)
            .enqueueUniqueWork(SYNC_FAVORITES_WORK, ExistingWorkPolicy.KEEP, workerRequest)
    }

    // Emits once for each sync that changed the local favorites.
    fun observeFavoritesChanged(): Flow<Unit> {
        return WorkManager.getInstance(_context)
            .getWorkInfosForUniqueWorkFlow(SYNC_FAVORITES_WORK)
            .mapNotNull { it.firstOrNull() }
            .filter { it.state == WorkInfo.State.SUCCEEDED }
            .distinctUntilChangedBy { it.id }
            .filter { it.outputData.getBoolean(SyncFavoritesWorker.HAS_CHANGES, false) }
            .map { }
    }

    private inline fun <reified T : CoroutineWorker> makeOneTime() {
        val workerRequest = OneTimeWorkRequestBuilder<T>().build()
        WorkManager.getInstance(_context).enqueue(workerRequest)
    }

    private companion object {
        const val SYNC_FAVORITES_WORK = "sync_favorites"
    }
}
