package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.network.response.ErrorResponse
import com.haroldadmin.cnradapter.NetworkResponse
import io.mockk.mockk
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Test

class HelpersTest {

    @Test
    fun `responseToResult should return Success when NetworkResponse is Success`() {
        val response = mockk<NetworkResponse.Success<String>>()
        io.mockk.every { response.body } returns "Data"
        val result = responseToResult(response)
        result shouldBeInstanceOf DataResult.Success::class
    }

    @Test
    fun `responseToResult should return Error when NetworkResponse is ServerError`() {
        val response = mockk<NetworkResponse.ServerError<ErrorResponse>>()
        val result = responseToResult(response)
        result shouldBeInstanceOf DataResult.Error::class
    }

    @Test
    fun `responseToResult should return Error when NetworkResponse is NetworkError`() {
        val response = mockk<NetworkResponse.NetworkError>()
        val result = responseToResult(response)
        result shouldBeInstanceOf DataResult.Error::class
    }

    @Test
    fun `responseToResult should return Error when NetworkResponse is UnknownError`() {
        val response = mockk<NetworkResponse.UnknownError>()
        val result = responseToResult(response)
        result shouldBeInstanceOf DataResult.Error::class
    }
}
