package br.dev.singular.overview.data.source.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.dev.singular.overview.data.api.ApiLocale
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.data.source.CacheDataSource
import br.dev.singular.overview.data.source.CacheDataSource.Companion.KEY_SELECTED_STREAMING_CACHE
import br.dev.singular.overview.data.source.streaming.IStreamingRemoteDataSource
import br.dev.singular.overview.data.source.streaming.StreamingLocalDataSource
import br.dev.singular.overview.di.StreamingsRemote
import br.dev.singular.overview.remote.RemoteConfig
import br.dev.singular.overview.util.fromJson
import br.dev.singular.overview.util.toJson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class StreamingSaveWorker @AssistedInject constructor(
    locale: ApiLocale,
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val _cache: CacheDataSource,
    private val _sourceLocal: StreamingLocalDataSource,
    private val _sourceRemote: IStreamingRemoteDataSource,
    @StreamingsRemote
    private val _remoteConfig: RemoteConfig<List<StreamingEntity>>
) : CoroutineWorker(context, params) {

    private val _region: String = locale.region

    private val _streamingCustom: List<StreamingEntity> by lazy { _remoteConfig.execute() }

    override suspend fun doWork(): Result {
        if (_streamingCustom.isNotEmpty()) {
            saveSelectedStreaming(streams = _streamingCustom)
            _sourceLocal.upgrade(_streamingCustom)
            return Result.success()
        } else {
            val streamingApi = getApiStreaming()
            if (streamingApi.isNotEmpty()) {
                saveSelectedStreaming(streams = streamingApi)
                _sourceLocal.upgrade(streamingApi)
                return Result.success()
            }
        }
        return Result.failure()
    }

    // TODO: test this
    private suspend fun saveSelectedStreaming(streams: List<StreamingEntity>) {
        if (streams.isNotEmpty()) {
            getSelectedStreaming { streamCache ->
                val streamMemory = streams.firstOrNull { it.apiId == streamCache?.apiId }
                val streamMemoryUnselected = streamMemory?.selected?.not() == true
                val saveCache = streamCache == null || streamMemoryUnselected
                if (saveCache) {
                    val streamJson = streams.firstOrNull { it.selected }.toJson()
                    _cache.setValue(KEY_SELECTED_STREAMING_CACHE, streamJson)
                }
            }
        }
    }

    // TODO: test this
    private suspend fun getSelectedStreaming(callback: suspend (StreamingEntity?) -> Unit) {
        _cache.getValue(KEY_SELECTED_STREAMING_CACHE).collect { streamingJson ->
            callback(streamingJson?.fromJson())
        }
    }

    private suspend fun getApiStreaming() = _sourceRemote.getItems()
        .filter { it.displayPriorities.containsKey(_region) }
        .sortedBy { it.displayPriorities[_region] }
}
