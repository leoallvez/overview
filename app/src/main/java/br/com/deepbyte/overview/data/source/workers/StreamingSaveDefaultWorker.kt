package br.com.deepbyte.overview.data.source.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.deepbyte.overview.data.api.ApiLocale
import br.com.deepbyte.overview.data.source.streaming.IStreamingRemoteDataSource
import br.com.deepbyte.overview.data.source.streaming.StreamingLocalDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class StreamingSaveDefaultWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    val _locale: ApiLocale,
    private val _sourceLocal: StreamingLocalDataSource,
    private val _sourceRemote: IStreamingRemoteDataSource
) : CoroutineWorker(context, params) {

    private val region = _locale.region
    override suspend fun doWork(): Result {
        val streamings = _sourceRemote.getItems()
        val streamingsByRegion = streamings.filter { it.displayPriorities.containsKey(region) }
        return if (streamingsByRegion.isNotEmpty()) {
            _sourceLocal.deleteAll()
            _sourceLocal.insert(*streamingsByRegion.toTypedArray())
            Timber.tag("work_manager").i(message = "StreamingDefaultSetupWorker done")
            Result.success()
        } else {
            Timber.tag("work_manager").i(message = "StreamingDefaultSetupWorker failed")
            Result.failure()
        }
    }
}
