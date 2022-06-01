package io.github.leoallvez.take.data.source.mediaitem

import com.haroldadmin.cnradapter.NetworkResponse
import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.source.MediaResult
import io.github.leoallvez.take.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaRemoteDataSource @Inject constructor(
    private val api: ApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun get(url: String): MediaResult {
        return withContext(ioDispatcher) {
            when (val response = api.getMediaItems(url)) {
                is NetworkResponse.Success -> MediaResult.ApiSuccess(
                    items = response.body.results
                )
                is NetworkResponse.ServerError -> MediaResult.ApiError(
                    code = response.code,
                    message = response.body?.statusMessage
                )
                is NetworkResponse.NetworkError -> MediaResult.ApiNetworkError
                is NetworkResponse.UnknownError -> MediaResult.ApiUnknownError
            }
        }
    }
}
