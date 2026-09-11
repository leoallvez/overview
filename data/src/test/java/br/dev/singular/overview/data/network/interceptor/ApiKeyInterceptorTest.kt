package br.dev.singular.overview.data.network.interceptor

import br.dev.singular.overview.data.BuildConfig
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import okhttp3.Interceptor
import okhttp3.Request
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class ApiKeyInterceptorTest {

    @Test
    fun `intercept should add api_key query parameter`() {
        // arrange
        val interceptor = ApiKeyInterceptor()
        val chain = mockk<Interceptor.Chain>()
        val request = Request.Builder()
            .url("https://api.test.com/")
            .build()
        val requestSlot = slot<Request>()

        every { chain.request() } returns request
        every { chain.proceed(capture(requestSlot)) } returns mockk()

        // act
        interceptor.intercept(chain)

        // assert
        val capturedRequest = requestSlot.captured
        capturedRequest.url.queryParameter("api_key") shouldBeEqualTo BuildConfig.API_KEY
    }
}
