package io.github.leoallvez.take.data.source.audiovisual

import com.haroldadmin.cnradapter.NetworkResponse.*
import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.source.AudiovisualResult
import io.github.leoallvez.take.data.source.AudiovisualResult.*
import io.github.leoallvez.take.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TvShowRemoteDataSource @Inject constructor(
    private val api: ApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun get(url: String): AudiovisualResult {
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

}