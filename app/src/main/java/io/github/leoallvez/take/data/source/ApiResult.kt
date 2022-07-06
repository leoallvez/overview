package io.github.leoallvez.take.data.source

sealed class ApiResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading<T>: ApiResult<T>()
    class Success<T>(data: T): ApiResult<T>(data)
    class ServerError<T>(message: String? = ""): ApiResult<T>(message = message)
    class NetworkError<T> : ApiResult<T>()
    class UnknownError<T> : ApiResult<T>()
}