package io.github.leoallvez.take.data.source.movie

import com.haroldadmin.cnradapter.NetworkResponse.*
import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.model.Movie
import io.github.leoallvez.take.data.source.AudiovisualResult
import io.github.leoallvez.take.data.source.AudiovisualResult.*
import io.github.leoallvez.take.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
    private val api: ApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun get(url: String): AudiovisualResult {
        return withContext(ioDispatcher) {
            when (val response = api.getMovies(url)) {
                is Success -> ApiSuccess(
                    content = response.body.results
                )
                is ServerError -> ApiError(
                    code = response.code,
                    message = response.body?.statusMessage
                )
                is NetworkError -> ApiNetworkError
                is UnknownError -> ApiUnknownError
            }
        }
    }
}
