package io.github.leoallvez.take.data.source

import io.github.leoallvez.take.data.model.MediaItem

sealed class MediaResult {

    class ApiSuccess(val items: List<MediaItem>) : MediaResult()

    class ApiError(val code: Int, val message: String?) : MediaResult()

    object ApiNetworkError : MediaResult()

    object ApiUnknownError : MediaResult()
}

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading<T>: NetworkResult<T>()
    class Success<T>(data: T): NetworkResult<T>(data)
    class Error<T>(message: String?): NetworkResult<T>(message = message)
    class NetworkError<T> : NetworkResult<T>()
    class UnknownError<T> : NetworkResult<T>()
}