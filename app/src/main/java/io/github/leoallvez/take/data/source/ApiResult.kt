package io.github.leoallvez.take.data.source

import com.haroldadmin.cnradapter.NetworkResponse
import io.github.leoallvez.take.data.api.response.ErrorResponse

sealed class ApiResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : ApiResult<T>(data)
    class ServerError <T>(message: String? = DEFAULT_MSG) : ApiResult<T>(message = message)
    class NetworkError<T>(message: String? = DEFAULT_MSG) : ApiResult<T>(message = message)
    class UnknownError<T>(message: String? = DEFAULT_MSG) : ApiResult<T>(message = message)

    companion object {
        private const val DEFAULT_MSG = ""
    }
}

//TODO: log in crashlytics the NetworkResponse Error
fun <T : Any> parserResponseToResult(
    response: NetworkResponse<T, ErrorResponse>
) = when(response) {
    is NetworkResponse.Success      -> ApiResult.Success(response.body)
    is NetworkResponse.ServerError  -> ApiResult.ServerError (message = response.body?.message)
    is NetworkResponse.NetworkError -> ApiResult.NetworkError(message = response.error.message)
    is NetworkResponse.UnknownError -> ApiResult.UnknownError(message = response.error.message)
}
