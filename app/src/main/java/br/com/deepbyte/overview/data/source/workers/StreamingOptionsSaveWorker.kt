package br.com.deepbyte.overview.data.source.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.deepbyte.overview.remote.RemoteConfig
import br.com.deepbyte.overview.data.api.ApiLocale
import br.com.deepbyte.overview.data.model.provider.Streaming
import br.com.deepbyte.overview.data.StreamingOptions
import br.com.deepbyte.overview.data.source.streaming.StreamingLocalDataSource
import br.com.deepbyte.overview.di.StreamingsRemote
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class StreamingOptionsSaveWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val _locale: ApiLocale,
    private val _sourceLocal: StreamingLocalDataSource,
    @StreamingsRemote
    private val _remoteConfig: RemoteConfig<StreamingOptions>
) : CoroutineWorker(context, params) {

    private val _options: StreamingOptions by lazy { _remoteConfig.execute() }

    override suspend fun doWork(): Result {
        if (getStreamingByRegion().isNullOrEmpty()) {
            _sourceLocal.upgrade(getStreamingByRegion())
            return Result.success()
        }
        return Result.failure()
    }

    private fun getStreamingByRegion(): List<Streaming>? {
        return _options.streamingsByRegion[_locale.region]
    }
}
