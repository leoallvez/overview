package br.dev.singular.overview.data.source.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.dev.singular.overview.data.api.ApiLocale
import br.dev.singular.overview.data.model.provider.StreamingEntity as Streaming
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
    private val _cacheDataSource: CacheDataSource,
    private val _sourceLocal: StreamingLocalDataSource,
    private val _sourceRemote: IStreamingRemoteDataSource,
    @StreamingsRemote
    private val _remoteConfig: RemoteConfig<List<Streaming>>
) : CoroutineWorker(context, params) {

    private val _region: String = locale.region

    private val _remoteStreams: List<Streaming> by lazy { _remoteConfig.execute() }

    override suspend fun doWork() = if (_remoteStreams.isNotEmpty()) {
        saveData(streams = _remoteStreams)
        Result.success()
    } else {
        val apiStreams = getApiStream()
        if (apiStreams.isNotEmpty()) {
            saveData(streams = apiStreams)
            Result.success()
        } else {
            Result.failure()
        }
    }

    private suspend fun saveData(streams: List<Streaming>) {
        saveStreamCache(allStreams = _remoteStreams)
        _sourceLocal.upgrade(streams)
    }

    private suspend fun saveStreamCache(allStreams: List<Streaming>) {
        if (allStreams.isNotEmpty()) {
            getStreamCache { streamCache ->
                if (shouldSaveCache(streamCache, allStreams)) {
                    val steam = allStreams.sortedBy { it.priority }.find { it.selected }
                    setSteamJsonCache(json = steam.toJson())
                }
            }
        }
    }

    private fun shouldSaveCache(stream: Streaming?, streams: List<Streaming>): Boolean {
        return stream == null || streams.none { it.apiId == stream.apiId && it.selected }
    }

    private suspend fun setSteamJsonCache(json: String) =
        _cacheDataSource.setValue(KEY_SELECTED_STREAMING_CACHE, json)

    private suspend fun getStreamCache(callback: suspend (Streaming?) -> Unit) {
        _cacheDataSource.getValue(KEY_SELECTED_STREAMING_CACHE).collect { streamJson ->
            callback(streamJson?.fromJson())
        }
    }

    private suspend fun getApiStream() = _sourceRemote.getItems()
        .filter { it.displayPriorities.containsKey(_region) }
        .sortedBy { it.displayPriorities[_region] }
}
