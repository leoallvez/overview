package br.dev.singular.overview.data.source.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.dev.singular.overview.data.api.ApiLocale
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.data.source.streaming.IStreamingRemoteDataSource
import br.dev.singular.overview.data.source.streaming.StreamingLocalDataSource
import br.dev.singular.overview.di.StreamingsRemote
import br.dev.singular.overview.remote.RemoteConfig
import br.dev.singular.overview.util.toJson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class StreamingSaveWorker @AssistedInject constructor(
    locale: ApiLocale,
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val _sourceLocal: StreamingLocalDataSource,
    private val _sourceRemote: IStreamingRemoteDataSource,
    @StreamingsRemote
    private val _remoteConfig: RemoteConfig<List<StreamingEntity>>
) : CoroutineWorker(context, params) {

    private val _region: String = locale.region

    private val _streamingCustom: List<StreamingEntity> by lazy { _remoteConfig.execute() }

    override suspend fun doWork(): Result {
        if (_streamingCustom.isNotEmpty()) {
            val json = _streamingCustom.first().toJson()
            Timber.i("json: $json")
            _sourceLocal.upgrade(_streamingCustom)
            return Result.success()
        } else {
            val streamingApi = getApiStreaming()
            if (streamingApi.isNotEmpty()) {
                _sourceLocal.upgrade(streamingApi)
                return Result.success()
            }
        }
        return Result.failure()
    }

    private suspend fun getApiStreaming() = _sourceRemote.getItems()
        .filter { it.displayPriorities.containsKey(_region) }
        .sortedBy { it.displayPriorities[_region] }
}
