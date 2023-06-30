package br.com.deepbyte.overview.data.repository.streaming

import br.com.deepbyte.overview.data.source.streaming.IStreamingRemoteDataSource
import br.com.deepbyte.overview.data.source.streaming.StreamingLocalDataSource
import br.com.deepbyte.overview.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StreamingRepository @Inject constructor(
    private val _remoteDataSource: IStreamingRemoteDataSource,
    private val _localDataSource: StreamingLocalDataSource,
    @IoDispatcher private val _dispatcher: CoroutineDispatcher
) : IStreamingRepository {

    override suspend fun getItems() = withContext(_dispatcher) {
        val result = _remoteDataSource.getItems()
        flow { emit(result) }
    }

    override suspend fun getAllSelected() = _localDataSource.getAllSelected()
}
