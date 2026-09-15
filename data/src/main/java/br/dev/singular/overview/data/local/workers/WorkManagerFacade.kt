package br.dev.singular.overview.data.local.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class WorkManagerFacade(
    private val _context: Context
) {
    private val workManager get() = WorkManager.getInstance(_context)

    fun init() {
        scheduleDeleteSuggestionsCacheTask()
        scheduleDeleteCatalogCacheTask()
        scheduleSaveGenreCacheTask()
    }

    private fun scheduleDeleteSuggestionsCacheTask() = makeOneTime<DeleteSuggestionsCacheWorker>()

    private fun scheduleDeleteCatalogCacheTask() = makeOneTime<DeleteCatalogCacheWorker>()

    private fun scheduleSaveGenreCacheTask() = makeOneTime<SaveGenreCacheWorker>()

    private inline fun <reified T : CoroutineWorker> makeOneTime() {
        val workerRequest = OneTimeWorkRequestBuilder<T>().build()
        workManager.enqueue(workerRequest)
    }
}
