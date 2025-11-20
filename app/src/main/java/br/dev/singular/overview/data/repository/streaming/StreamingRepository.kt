package br.dev.singular.overview.data.repository.streaming

import br.dev.singular.overview.data.local.source.CacheDataSource
import br.dev.singular.overview.data.local.source.CacheDataSource.Companion.KEY_SELECTED_STREAMING_CACHE
import br.dev.singular.overview.data.model.provider.StreamingData
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.data.repository.streaming.selected.ISelectedStreamingRepository
import br.dev.singular.overview.data.source.streaming.IStreamingRemoteDataSource
import br.dev.singular.overview.data.source.streaming.StreamingLocalDataSource
import br.dev.singular.overview.di.IoDispatcher
import br.dev.singular.overview.util.fromJson
import br.dev.singular.overview.util.toJson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StreamingRepository @Inject constructor(
    private val _cacheDataSource: CacheDataSource,
    private val _localDataSource: StreamingLocalDataSource,
    private val _remoteDataSource: IStreamingRemoteDataSource,
    @param:IoDispatcher private val _dispatcher: CoroutineDispatcher
) : IStreamingRepository, ISelectedStreamingRepository {

    override suspend fun getItems() = withContext(_dispatcher) {
        val result = _localDataSource.getItems()
        flow { emit(result) }
    }

    override suspend fun getAllSelected() = withContext(_dispatcher) {
        val result = _localDataSource.getAllSelected()
        flow { emit(result) }
    }

    override suspend fun getAllLocal() = withContext(_dispatcher) {
        val result = _localDataSource.getItems()
        flow { emit(result.streamingData()) }
    }

    override suspend fun getAllRemote(region: String) = withContext(_dispatcher) {
        _remoteDataSource.getItems()
            .filter { it.displayPriorities.containsKey(region) }
            .sortedBy { it.displayPriorities[region] }
    }

    override suspend fun updateAllLocal(streaming: List<StreamingEntity>) {
        _localDataSource.upgrade(streaming)
    }

    private fun List<StreamingEntity>.streamingData() = StreamingData(list = this)

    override suspend fun getSelectedItem() = flow {
        _cacheDataSource.getValue(KEY_SELECTED_STREAMING_CACHE).collect { json ->
            emit(json?.fromJson<StreamingEntity>())
        }
    }

    override suspend fun updateSelected(streaming: StreamingEntity?) {
        _cacheDataSource.setValue(KEY_SELECTED_STREAMING_CACHE, streaming.toJson())
    }
}
