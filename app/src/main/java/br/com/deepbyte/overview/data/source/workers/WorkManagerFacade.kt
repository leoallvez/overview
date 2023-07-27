package br.com.deepbyte.overview.data.source.workers

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class WorkManagerFacade constructor(
    private val _context: Context
) {
    fun init() {
        scheduleStreamingOptionsSaveTask()
        scheduleGenreDefaultTask()
    }

    private fun scheduleStreamingOptionsSaveTask() {
        val workerRequest = OneTimeWorkRequestBuilder<StreamingOptionsSaveWorker>().build()
        WorkManager.getInstance(_context).enqueue(workerRequest)
    }

    private fun scheduleGenreDefaultTask() {
        val workerRequest = OneTimeWorkRequestBuilder<GenreDefaultSetupWorker>().build()
        WorkManager.getInstance(_context).enqueue(workerRequest)
    }
}
