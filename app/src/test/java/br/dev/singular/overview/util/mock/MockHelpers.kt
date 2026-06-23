package br.dev.singular.overview.util.mock

import br.dev.singular.overview.data.api.response.ErrorResponse
import com.haroldadmin.cnradapter.NetworkResponse
import java.io.IOException

const val ERROR_MSG = "Error"

fun <T : Any> mockResponse(
    returnType: ReturnType,
    successResponse: NetworkResponse.Success<T>
) = when (returnType) {
    ReturnType.SUCCESS -> successResponse
    ReturnType.SERVER_ERROR -> getServerErrorResponse()
    ReturnType.NETWORK_ERROR -> NetworkResponse.NetworkError(IOException(ERROR_MSG))
    ReturnType.UNKNOWN_ERROR -> NetworkResponse.UnknownError(Throwable(ERROR_MSG))
}

private fun getServerErrorResponse() = NetworkResponse.ServerError(
    body = ErrorResponse(success = false, code = 500, message = ERROR_MSG),
    code = 500
)

enum class ReturnType {
    SUCCESS, SERVER_ERROR, NETWORK_ERROR, UNKNOWN_ERROR
}
