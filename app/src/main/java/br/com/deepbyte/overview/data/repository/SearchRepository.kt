package br.com.deepbyte.overview.data.repository

import br.com.deepbyte.overview.data.MediaType
import br.com.deepbyte.overview.data.source.search.ISearchRemoteDataSource
import br.com.deepbyte.overview.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val _remoteDataSource: ISearchRemoteDataSource,
    @IoDispatcher private val _dispatcher: CoroutineDispatcher
) {

    suspend fun searchTV(query: String, page: Int) = withContext(_dispatcher) {
        return@withContext flow {
            emit(_remoteDataSource.searchMedia(MediaType.TV.key, query, page))
        }
    }

    suspend fun searchMovie(query: String, page: Int) = withContext(_dispatcher) {
        return@withContext flow {
            emit(_remoteDataSource.searchMedia(MediaType.MOVIE.key, query, page))
        }
    }
}
