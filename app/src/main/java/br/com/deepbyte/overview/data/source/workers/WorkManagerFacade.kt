package br.com.deepbyte.overview.data.source.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class WorkManagerFacade constructor(
    private val _context: Context
) {
    fun init() {
        scheduleStreamingsSaveTask()
        scheduleGenreDefaultTask()
        scheduleMediaTask()
    }

    private fun scheduleStreamingsSaveTask() = makeOneTime<StreamingsSaveWorker>()

    private fun scheduleGenreDefaultTask() = makeOneTime<GenreDefaultSetupWorker>()

    private fun scheduleMediaTask() = makeOneTime<MediaWorker>()

    private inline fun <reified T : CoroutineWorker> makeOneTime() {
        val workerRequest = OneTimeWorkRequestBuilder<T>().build()
        WorkManager.getInstance(_context).enqueue(workerRequest)
    }
}
