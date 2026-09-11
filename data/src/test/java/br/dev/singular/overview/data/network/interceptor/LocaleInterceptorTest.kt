package br.dev.singular.overview.data.network.interceptor

import br.dev.singular.overview.data.network.ILocaleProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import okhttp3.Interceptor
import okhttp3.Request
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class LocaleInterceptorTest {

    @Test
    fun `intercept should add locale query parameters`() {
        // arrange
        val localeProvider = mockk<ILocaleProvider>()
        every { localeProvider.language } returns "pt-BR"
        every { localeProvider.region } returns "BR"

        val interceptor = LocaleInterceptor(localeProvider)
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
        capturedRequest.url.queryParameter("language") shouldBeEqualTo "pt-BR"
        capturedRequest.url.queryParameter("region") shouldBeEqualTo "BR"
        capturedRequest.url.queryParameter("watch_region") shouldBeEqualTo "BR"
    }
}
