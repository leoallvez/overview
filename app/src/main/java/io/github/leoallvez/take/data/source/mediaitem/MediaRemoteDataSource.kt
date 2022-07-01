package io.github.leoallvez.take.data.source.mediaitem

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.NetworkResponse.*
import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.api.response.ErrorResponse
import io.github.leoallvez.take.data.api.response.ListContentResponse
import io.github.leoallvez.take.data.api.response.MediaDetailResponse
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.source.MediaResult
import io.github.leoallvez.take.data.source.NetworkResult
import io.github.leoallvez.take.di.IoDispatcher
import io.github.leoallvez.take.util.MediaDetailResult
import io.github.leoallvez.take.util.MediaListResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

// https://medium.com/codex/kotlin-sealed-classes-for-better-handling-of-api-response-6aa1fbd23c76
class MediaRemoteDataSource @Inject constructor(
    private val api: ApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getMediaItems(url: String): MediaListResult {
        return withContext(ioDispatcher) {
            when(val response = api.getMediaItems(url)) {
                is Success -> NetworkResult.Success(response.body)
                is ServerError  -> {
                    val msg = response.body?.statusMessage
                    NetworkResult.Error(message = msg)
                }
                is NetworkError -> NetworkResult.NetworkError()
                is UnknownError -> NetworkResult.UnknownError()
            }
        }
    }

    suspend fun getMediaDetails(id: Long, type: String): MediaDetailResult  {
        return withContext(ioDispatcher) {
            when(val response = api.getMediaDetail(id = id, type = type)) {
                is Success -> {
                    NetworkResult.Success(response.body)
                }
                is ServerError  -> {
                    NetworkResult.Error(message = response.body?.statusMessage)
                }
                is NetworkError -> NetworkResult.NetworkError()
                is UnknownError -> NetworkResult.UnknownError()
            }
        }
    }
}
