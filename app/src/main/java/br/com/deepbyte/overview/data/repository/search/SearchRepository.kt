package br.com.deepbyte.overview.data.repository.search

import br.com.deepbyte.overview.data.MediaType
import br.com.deepbyte.overview.data.source.media.IMediaRemoteDataSource
import br.com.deepbyte.overview.data.source.person.IPersonRemoteDataSource
import br.com.deepbyte.overview.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val _mediaSource: IMediaRemoteDataSource,
    private val _personSource: IPersonRemoteDataSource,
    @IoDispatcher private val _dispatcher: CoroutineDispatcher
) : ISearchRepository {

    override suspend fun search(query: String) = withContext(_dispatcher) {
        val results = getSearchResults(query)
        flow { emit(results) }
    }

    private suspend fun getSearchResults(query: String): SearchResult {

        val moviesResult = _mediaSource.search(MediaType.MOVIE.key, query)
        val tvShowsResult = _mediaSource.search(MediaType.TV.key, query)
        val personsResult = _personSource.search(query)

        return SearchResult(moviesResult, tvShowsResult, personsResult)
    }
}
