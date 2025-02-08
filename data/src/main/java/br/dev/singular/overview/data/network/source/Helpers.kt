package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.network.response.ErrorResponse
import com.haroldadmin.cnradapter.NetworkResponse

sealed class DataResult<T> {
    data class Success<T>(val data: T) : DataResult<T>()
    data class Error<T>(val message: String = "Unknown error") : DataResult<T>()
}

fun <T : Any> responseToResult(
    response: NetworkResponse<T, ErrorResponse>
) = when (response) {
    is NetworkResponse.Success -> DataResult.Success(data = response.body)
    is NetworkResponse.ServerError -> DataResult.Error(message = response.body?.message.orEmpty())
    is NetworkResponse.NetworkError -> DataResult.Error(message = response.error.message.orEmpty())
    is NetworkResponse.UnknownError -> DataResult.Error(message = response.error.message.orEmpty())
}
