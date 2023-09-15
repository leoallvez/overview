package dev.com.singular.overview.data.source.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker.Result.failure
import androidx.work.ListenableWorker.Result.success
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.com.singular.overview.data.repository.media.interfaces.IMediaCacheRepository

@HiltWorker
class MediaCacheWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: IMediaCacheRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork() = if (repository.saveMediaCache()) success() else failure()
}
