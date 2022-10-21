package io.github.leoallvez.take.util.mock

import com.haroldadmin.cnradapter.NetworkResponse
import io.github.leoallvez.take.data.api.response.ErrorResponse
import io.github.leoallvez.take.data.source.DataResult
import java.io.IOException

object MockResponse {
    const val ERROR_MSG = "Error"
    val networkResponse = NetworkResponse.NetworkError(IOException(ERROR_MSG))
    val serverErrorResponse = NetworkResponse.ServerError(
        body = ErrorResponse(success = false, code = 500, message = ERROR_MSG),
        code = 500
    )
    val unknownErrorResponse = NetworkResponse.UnknownError(Throwable(ERROR_MSG))
}

fun <T> mockResult(
    returnType: ReturnType,
    successResult: DataResult.Success<T>
) = when(returnType) {
    ReturnType.SUCCESS       -> successResult
    ReturnType.SERVER_ERROR  -> DataResult.ServerError()
    ReturnType.NETWORK_ERROR -> DataResult.NetworkError()
    ReturnType.UNKNOWN_ERROR -> DataResult.UnknownError()
}

enum class ReturnType {
    SUCCESS, SERVER_ERROR, NETWORK_ERROR, UNKNOWN_ERROR
}