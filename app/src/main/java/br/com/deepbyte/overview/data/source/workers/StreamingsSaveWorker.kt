package br.com.deepbyte.overview.data.source.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.deepbyte.overview.data.model.provider.Streaming
import br.com.deepbyte.overview.data.source.streaming.IStreamingRemoteDataSource
import br.com.deepbyte.overview.data.source.streaming.StreamingLocalDataSource
import br.com.deepbyte.overview.di.StreamingsRemote
import br.com.deepbyte.overview.remote.RemoteConfig
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class StreamingsSaveWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val _sourceLocal: StreamingLocalDataSource,
    private val _sourceRemote: IStreamingRemoteDataSource,
    @StreamingsRemote
    private val _remoteConfig: RemoteConfig<List<Streaming>>
) : CoroutineWorker(context, params) {

    private val _streamingsCustom: List<Streaming> by lazy { _remoteConfig.execute() }

    override suspend fun doWork(): Result {
        if (_streamingsCustom.isNotEmpty()) {
            _sourceLocal.upgrade(_streamingsCustom)
            return Result.success()
        } else {
            val streamingsApi = _sourceRemote.getItems()
            if (streamingsApi.isNotEmpty()) {
                _sourceLocal.upgrade(streamingsApi)
                return Result.success()
            }
        }
        return Result.failure()
    }
}
