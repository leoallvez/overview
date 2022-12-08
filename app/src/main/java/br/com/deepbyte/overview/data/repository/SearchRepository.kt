package br.com.deepbyte.overview.data.repository

import br.com.deepbyte.overview.data.MediaType
import br.com.deepbyte.overview.data.repository.results.SearchResults
import br.com.deepbyte.overview.data.source.search.ISearchRemoteDataSource
import br.com.deepbyte.overview.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val _source: ISearchRemoteDataSource,
    @IoDispatcher private val _dispatcher: CoroutineDispatcher
) {

    suspend fun search(query: String) = withContext(_dispatcher) {
        return@withContext flow {
            emit(searchRequests(query))
        }
    }

    private suspend fun searchRequests(query: String) = _source.run {

        val moviesResult = searchMedia(MediaType.MOVIE.key, query)
        val tvShowsResult = searchMedia(MediaType.TV.key, query)
        val personsResult = searchPerson(query)

        return@run SearchResults(moviesResult, tvShowsResult, personsResult)
    }
}
