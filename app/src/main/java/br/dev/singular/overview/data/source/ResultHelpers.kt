package br.dev.singular.overview.data.source

import br.dev.singular.overview.data.api.response.ErrorResponse
import com.haroldadmin.cnradapter.NetworkResponse

sealed class DataResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : DataResult<T>(data = data)
    class ServerError<T>(message: String? = DEFAULT_MSG) : DataResult<T>(message = message)
    class NetworkError<T>(message: String? = DEFAULT_MSG) : DataResult<T>(message = message)
    class UnknownError<T>(message: String? = DEFAULT_MSG) : DataResult<T>(message = message)
    companion object {
        private const val DEFAULT_MSG = ""
    }
}

fun <T : Any> responseToResult(
    response: NetworkResponse<T, ErrorResponse>
) = when (response) {
    is NetworkResponse.Success -> DataResult.Success(data = response.body)
    is NetworkResponse.ServerError -> DataResult.ServerError(message = response.body?.message)
    is NetworkResponse.NetworkError -> DataResult.NetworkError(message = response.error.message)
    is NetworkResponse.UnknownError -> DataResult.UnknownError(message = response.error.message)
}
