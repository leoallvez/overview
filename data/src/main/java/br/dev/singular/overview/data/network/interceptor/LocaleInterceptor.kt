package br.dev.singular.overview.data.network.interceptor

import br.dev.singular.overview.data.network.ILocaleProvider
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class LocaleInterceptor @Inject constructor(
    private val locale: ILocaleProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url
        val url = originalUrl.newBuilder()
            .applyLanguageIfNeeded(originalUrl)
            .setQueryParameter(name = "region", value = locale.region)
            .setQueryParameter(name = "watch_region", value = locale.region)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(url)
            .build()

        return chain.proceed(newRequest)
    }

    private fun HttpUrl.Builder.applyLanguageIfNeeded(originalUrl: HttpUrl): HttpUrl.Builder =
        apply {
            if (originalUrl.queryParameter(LANGUAGE) == null) {
                setQueryParameter(name = LANGUAGE, value = locale.language)
            }
        }

    private companion object {
        const val LANGUAGE = "language"
    }
}
