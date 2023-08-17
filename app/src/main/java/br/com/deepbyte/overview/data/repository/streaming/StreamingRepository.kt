package br.com.deepbyte.overview.data.repository.streaming

import br.com.deepbyte.overview.data.model.provider.StreamingEntity
import br.com.deepbyte.overview.data.model.provider.StreamingsWrap
import br.com.deepbyte.overview.data.source.streaming.StreamingLocalDataSource
import br.com.deepbyte.overview.di.IoDispatcher
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

    override suspend fun getStreamingsWrap() = withContext(_dispatcher) {
        val result = _localDataSource.getItems()
        flow { emit(result.toStreamingsWrap()) }
    }

    private fun List<StreamingEntity>.toStreamingsWrap() =
        StreamingsWrap(selected = filter { it.selected }, unselected = filter { !it.selected })
}
