package br.dev.singular.overview.data.network.interceptor

import br.dev.singular.overview.data.network.ILocaleProvider
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class LocaleInterceptor @Inject constructor(
    private val locale: ILocaleProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.newBuilder()
            .setQueryParameter(name = "language", value = locale.language)
            .setQueryParameter(name = "region", value = locale.region)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(url)
            .build()

        return chain.proceed(newRequest)
    }
}