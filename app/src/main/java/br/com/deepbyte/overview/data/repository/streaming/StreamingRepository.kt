package br.com.deepbyte.overview.data.repository.streaming

import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.model.provider.Streaming
import br.com.deepbyte.overview.data.source.streaming.IStreamingRemoteDataSource
import br.com.deepbyte.overview.data.source.streaming.StreamingLocalDataSource
import br.com.deepbyte.overview.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StreamingRepository @Inject constructor(
    _locale: IApiLocale,
    private val _localDataSource: StreamingLocalDataSource,
    private val _remoteDataSource: IStreamingRemoteDataSource,
    @IoDispatcher private val _dispatcher: CoroutineDispatcher
) : IStreamingRepository {

    private val _country: String by lazy { _locale.region }

    override suspend fun getItems() = withContext(_dispatcher) {
        val result = _remoteDataSource.getItems()
        flow { emit(result) }
    }

    override suspend fun itemsFilteredByCurrentCountry() = withContext(_dispatcher) {
        val result = _remoteDataSource.getItems()
        flow { emit(result.filterByCountry()) }
    }

    private fun List<Streaming>.filterByCountry() =
        filter { it.displayPriorities.any { d -> d.key == _country } }
            .sortedBy { it.displayPriorities[_country] }

    override suspend fun getAllSelected() = _localDataSource.getAllSelected()
}
