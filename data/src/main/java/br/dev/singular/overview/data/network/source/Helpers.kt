package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.network.response.ErrorResponse
import com.haroldadmin.cnradapter.NetworkResponse

sealed class DataResult<T> {
    data class Success<T>(val data: T) : DataResult<T>()
    class Error<T> : DataResult<T>()
}

fun <T : Any> responseToResult(
    response: NetworkResponse<T, ErrorResponse>
) = when (response) {
    is NetworkResponse.Success -> DataResult.Success(data = response.body)
    is NetworkResponse.ServerError -> DataResult.Error()
    is NetworkResponse.NetworkError -> DataResult.Error()
    is NetworkResponse.UnknownError -> DataResult.Error()
}
