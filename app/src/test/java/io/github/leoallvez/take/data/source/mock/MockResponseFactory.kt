package io.github.leoallvez.take.data.source.mock

import com.haroldadmin.cnradapter.NetworkResponse
import io.github.leoallvez.take.data.api.response.ErrorResponse
import io.github.leoallvez.take.data.api.response.MediaDetailResponse
import okio.IOException

internal typealias Response = NetworkResponse<MediaDetailResponse, ErrorResponse>
internal typealias SuccessResponse = NetworkResponse.Success<MediaDetailResponse>

abstract class MockResponseFactory {

    abstract fun makeResponse(): Response

    companion object {
        inline fun <reified T : Response> createFactory() = when(T::class) {
            NetworkResponse.Success::class      -> MockSuccessFactory
            NetworkResponse.NetworkError::class -> MockNetworkErrorFactory
            NetworkResponse.ServerError::class  -> MockServerErrorFactory
            NetworkResponse.UnknownError::class -> MockUnknownErrorFactory
            else -> throw IllegalArgumentException()
        }
        fun createResponse() = MediaDetailResponse()
    }
}

object MockSuccessFactory : MockResponseFactory() {
    override fun makeResponse() = NetworkResponse.Success(body = createResponse(), code = 200)
}

object MockNetworkErrorFactory : MockResponseFactory() {
    override fun makeResponse() = NetworkResponse.NetworkError(error = IOException("network error"))
}

object MockServerErrorFactory : MockResponseFactory() {

    override fun makeResponse() =
        NetworkResponse.ServerError(body = makeServeErrorBody(), code = 500)

    private fun makeServeErrorBody() = ErrorResponse(
        success = false,
        statusCode = 500,
        statusMessage = "Serve error"
    )
}

object MockUnknownErrorFactory : MockResponseFactory() {
    override fun makeResponse() = NetworkResponse.UnknownError(Throwable("unknown error"))
}