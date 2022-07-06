package io.github.leoallvez.take.util.mock

import com.haroldadmin.cnradapter.NetworkResponse.*
import io.github.leoallvez.take.data.api.response.ErrorResponse
import io.github.leoallvez.take.data.api.response.MediaDetailResponse
import okio.IOException

abstract class MockResponseFactory {

    abstract fun makeResponse(): Response

    companion object {
        inline fun <reified T : Response> createFactory() = when(T::class) {
            Success::class      -> MockSuccessFactory
            NetworkError::class -> MockNetworkErrorFactory
            ServerError::class  -> MockServerErrorFactory
            UnknownError::class -> MockUnknownErrorFactory
            else -> throw IllegalArgumentException()
        }
        fun getDataResponse() = MediaDetailResponse()
        const val ERROR_MSG = "ERROR_MSG"
    }
}

object MockSuccessFactory : MockResponseFactory() {
    override fun makeResponse() = Success(body = getDataResponse(), code = 200)
}

object MockNetworkErrorFactory : MockResponseFactory() {
    override fun makeResponse() = NetworkError(error = IOException(ERROR_MSG))
}

object MockServerErrorFactory : MockResponseFactory() {

    override fun makeResponse() = ServerError(body = makeServeErrorBody(), code = 500)

    private fun makeServeErrorBody() = ErrorResponse().apply {
        success = false
        statusCode = 500
        statusMessage = ERROR_MSG
    }
}

object MockUnknownErrorFactory : MockResponseFactory() {
    override fun makeResponse() = UnknownError(Throwable(ERROR_MSG))
}
