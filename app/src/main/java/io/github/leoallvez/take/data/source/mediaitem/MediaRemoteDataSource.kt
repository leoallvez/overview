package io.github.leoallvez.take.data.source.mediaitem

import com.haroldadmin.cnradapter.NetworkResponse.*
import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.source.NetworkResult
import io.github.leoallvez.take.util.MediaDetailResult
import io.github.leoallvez.take.util.MediaListResult
import javax.inject.Inject

//TODO: remove repetition of when structure in methods
class MediaRemoteDataSource @Inject constructor(
    private val _api: ApiService
) : IMediaRemoteDataSource {

    override suspend fun getMediaItems(url: String) =
        when(val response = _api.getMediaItems(url)) {
            is Success      -> NetworkResult.Success(response.body)
            is ServerError  -> getServeError(msg = response.body?.statusMessage)
            is NetworkError -> NetworkResult.NetworkError()
            is UnknownError -> NetworkResult.UnknownError()
        }

    override suspend fun getMediaDetails(id: Long, type: String) =
        when(val response = _api.getMediaDetail(id = id, type = type)) {
            is Success      -> NetworkResult.Success(response.body)
            is ServerError  -> getServeError(msg = response.body?.statusMessage)
            is NetworkError -> NetworkResult.NetworkError()
            is UnknownError -> NetworkResult.UnknownError()
        }

    private fun <T> getServeError(msg: String?) = NetworkResult.Error<T>(msg)
}

interface IMediaRemoteDataSource {
    suspend fun getMediaItems(url: String): MediaListResult
    suspend fun getMediaDetails(id: Long, type: String): MediaDetailResult
}
