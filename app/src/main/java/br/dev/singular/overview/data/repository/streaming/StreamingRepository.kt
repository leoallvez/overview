package br.dev.singular.overview.data.repository.streaming

import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.data.model.provider.StreamingsData
import br.dev.singular.overview.data.source.streaming.StreamingLocalDataSource
import br.dev.singular.overview.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StreamingRepository @Inject constructor(
    private val _localDataSource: StreamingLocalDataSource,
    @IoDispatcher private val _dispatcher: CoroutineDispatcher
) : IStreamingRepository {

    override suspend fun getItems() = withContext(_dispatcher) {
        val result = _localDataSource.getItems()
        flow { emit(result) }
    }

    override suspend fun getAllSelected() = withContext(_dispatcher) {
        val result = _localDataSource.getAllSelected()
        flow { emit(result) }
    }

    override suspend fun getStreamingsData() = withContext(_dispatcher) {
        val result = _localDataSource.getItems()
        flow { emit(result.streamingsData()) }
    }

    private fun List<StreamingEntity>.streamingsData() =
        StreamingsData(selected = filter { it.selected }, unselected = filter { !it.selected })
}
