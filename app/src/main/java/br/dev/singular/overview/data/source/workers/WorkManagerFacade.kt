package br.dev.singular.overview.data.source.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class WorkManagerFacade(
    private val _context: Context
) {
    fun init() {
        scheduleStreamingSaveTask()
        scheduleGenreDefaultTask()
        scheduleMediaCacheTask()
    }

    private fun scheduleStreamingSaveTask() = makeOneTime<StreamingSaveWorker>()

    private fun scheduleGenreDefaultTask() = makeOneTime<GenreDefaultSetupWorker>()

    private fun scheduleMediaCacheTask() = makeOneTime<MediaCacheWorker2>()

    private inline fun <reified T : CoroutineWorker> makeOneTime() {
        val workerRequest = OneTimeWorkRequestBuilder<T>().build()
        WorkManager.getInstance(_context).enqueue(workerRequest)
    }
}
