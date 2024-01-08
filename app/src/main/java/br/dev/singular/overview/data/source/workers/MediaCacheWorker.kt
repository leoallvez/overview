package br.dev.singular.overview.data.source.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.dev.singular.overview.data.repository.media.interfaces.IMediaRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Calendar

@HiltWorker
class MediaCacheWorker2 @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: IMediaRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        repository.deleteOlderThan(date = getMaxCacheDate())
        return Result.success()
    }

    private fun getMaxCacheDate() = Calendar.getInstance().run {
        add(Calendar.DAY_OF_MONTH, AMOUNT_DAYS_OF_CACHE)
        time
    }

    private companion object {
        const val AMOUNT_DAYS_OF_CACHE = -7
    }
}
