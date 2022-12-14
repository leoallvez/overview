package br.com.deepbyte.overview.data.repository.search

import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.source.media.IMediaRemoteDataSource
import br.com.deepbyte.overview.data.source.person.IPersonRemoteDataSource
import br.com.deepbyte.overview.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val _movieSource: IMediaRemoteDataSource<Movie>,
    private val _tvShowSource: IMediaRemoteDataSource<TvShow>,
    private val _personSource: IPersonRemoteDataSource,
    @IoDispatcher private val _dispatcher: CoroutineDispatcher
) : ISearchRepository {

    override suspend fun search(query: String) = withContext(_dispatcher) {
        val results = getSearchResults(query)
        flow { emit(results) }
    }

    private suspend fun getSearchResults(query: String): SearchResult {

        val moviesResult = _movieSource.search(query)
        val tvShowsResult = _tvShowSource.search(query)
        val personsResult = _personSource.search(query)

        return SearchResult(moviesResult, tvShowsResult, personsResult)
    }
}
