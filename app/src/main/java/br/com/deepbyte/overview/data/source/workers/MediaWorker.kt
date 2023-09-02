package br.com.deepbyte.overview.data.source.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker.Result.success
import androidx.work.WorkerParameters
import br.com.deepbyte.overview.data.repository.media.interfaces.IMediaCacheRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class MediaWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: IMediaCacheRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork() = success() //if (repository.saveCache()) success() else failure()
}
