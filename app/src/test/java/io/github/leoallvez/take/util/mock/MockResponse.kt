package io.github.leoallvez.take.util.mock

import com.haroldadmin.cnradapter.NetworkResponse
import io.github.leoallvez.take.data.api.response.ErrorResponse
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