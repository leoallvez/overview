package io.github.leoallvez.take.util.mock

import com.haroldadmin.cnradapter.NetworkResponse
import io.github.leoallvez.take.data.api.response.ErrorResponse
import io.github.leoallvez.take.data.source.DataResult
import java.io.IOException

fun <T> mockResult(
    returnType: ReturnType,
    successResult: DataResult.Success<T>
) = when(returnType) {
    ReturnType.SUCCESS       -> successResult
    ReturnType.SERVER_ERROR  -> DataResult.ServerError()
    ReturnType.NETWORK_ERROR -> DataResult.NetworkError()
    ReturnType.UNKNOWN_ERROR -> DataResult.UnknownError()
}

const val ERROR_MSG = "Error"

fun <T: Any> mockResponse(
    returnType: ReturnType,
    successResponse: NetworkResponse.Success<T>
) = when(returnType) {
    ReturnType.SUCCESS       -> successResponse
    ReturnType.SERVER_ERROR  -> getServerErrorResponse()
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