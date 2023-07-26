package br.com.deepbyte.overview.data.source.workers

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class WorkManagerFacade constructor(
    private val _context: Context
) {
    fun init() {
        scheduleStreamingSaveDefaultTask()
        scheduleGenreDefaultTask()
    }

    private fun scheduleStreamingSaveDefaultTask() {
        val workerRequest = OneTimeWorkRequestBuilder<StreamingOptionsSaveWorker>().build()
        WorkManager.getInstance(_context).enqueue(workerRequest)
    }

    private fun scheduleGenreDefaultTask() {
        val workerRequest = OneTimeWorkRequestBuilder<GenreDefaultSetupWorker>().build()
        WorkManager.getInstance(_context).enqueue(workerRequest)
    }
}
