package br.com.deepbyte.overview.data.source.workers

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class WorkManagerFacade constructor(
    private val _context: Context
) {
    fun init() {
        scheduleStreamingDefaultTask()
        scheduleStreamingUpdateTask()
        scheduleGenreDefaultTask()
    }

    private fun scheduleStreamingDefaultTask() {
        val workerRequest = OneTimeWorkRequestBuilder<StreamingDefaultSetupWorker>().build()
        WorkManager.getInstance(_context).enqueue(workerRequest)
    }

    private fun scheduleStreamingUpdateTask() {
        val workerRequest = PeriodicWorkRequest
            .Builder(
                StreamingSelectedUpdateWorker::class.java,
                repeatInterval = 24,
                TimeUnit.HOURS
            ).build()
        WorkManager.getInstance(_context).enqueue(workerRequest)
    }

    private fun scheduleGenreDefaultTask() {
        val workerRequest = OneTimeWorkRequestBuilder<GenreDefaultSetupWorker>().build()
        WorkManager.getInstance(_context).enqueue(workerRequest)
    }
}
