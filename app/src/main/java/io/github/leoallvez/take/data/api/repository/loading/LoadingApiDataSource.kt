package io.github.leoallvez.take.data.api.repository.loading

import com.haroldadmin.cnradapter.NetworkResponse.*
import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.api.repository.loading.LoadingApiDataSource.ContentResult.*
import io.github.leoallvez.take.data.model.Audiovisual
import io.github.leoallvez.take.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadingApiDataSource @Inject constructor(
    private val api: ApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getTvShows(url: String): ContentResult {
        return withContext(ioDispatcher) {
            when (val response = api.getTvShows(url)) {
                is Success -> ApiSuccess(
                    result = response.body.results
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

    suspend fun getMovies(url: String): ContentResult {
        return withContext(ioDispatcher) {
            when (val response = api.getMovies(url)) {
                is Success -> ApiSuccess(
                    result = response.body.results
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

    sealed class ContentResult {

        class ApiSuccess(
            result: List<Audiovisual>
        ) : ContentResult()

        class ApiError(
            val code: Int,
            val message: String?
        ) : ContentResult()

        object ApiNetworkError : ContentResult()

        object ApiUnknownError : ContentResult()
    }
}
