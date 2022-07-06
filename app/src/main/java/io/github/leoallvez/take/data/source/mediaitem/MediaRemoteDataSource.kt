package io.github.leoallvez.take.data.source.mediaitem

import com.haroldadmin.cnradapter.NetworkResponse.*
import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.api.response.ListContentResponse
import io.github.leoallvez.take.data.api.response.MediaDetailResponse
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.source.ApiResult
import javax.inject.Inject

//TODO: remove repetition of when structure in methods
class MediaRemoteDataSource @Inject constructor(
    private val _api: ApiService
) : IMediaRemoteDataSource {

    override suspend fun getMediaItems(url: String) =
        when(val response = _api.getMediaItems(url)) {
            is Success      -> ApiResult.Success(response.body)
            is ServerError  -> getServeError(msg = response.body?.statusMessage)
            is NetworkError -> ApiResult.NetworkError()
            is UnknownError -> ApiResult.UnknownError()
        }

    override suspend fun getMediaDetailsResult(id: Long, type: String) =
        when(val response = _api.requestMediaDetail(id = id, type = type)) {
            is Success      -> ApiResult.Success(response.body)
            is ServerError  -> getServeError(msg = response.body?.statusMessage)
            is NetworkError -> ApiResult.NetworkError()
            is UnknownError -> ApiResult.UnknownError()
        }

    private fun <T> getServeError(msg: String?) = ApiResult.ServerError<T>(msg)
}

interface IMediaRemoteDataSource {
    suspend fun getMediaItems(url: String): ApiResult<ListContentResponse<MediaItem>>
    suspend fun getMediaDetailsResult(id: Long, type: String): ApiResult<MediaDetailResponse>
}
