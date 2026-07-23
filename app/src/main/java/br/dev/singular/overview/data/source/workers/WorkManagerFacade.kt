package br.dev.singular.overview.data.source.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

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

    private inline fun <reified T : CoroutineWorker> makeOneTime() {
        val workerRequest = OneTimeWorkRequestBuilder<T>().build()
        WorkManager.getInstance(_context).enqueue(workerRequest)
    }
}
