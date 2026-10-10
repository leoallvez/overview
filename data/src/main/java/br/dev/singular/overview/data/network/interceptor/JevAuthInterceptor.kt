package br.dev.singular.overview.data.network.interceptor

import br.dev.singular.overview.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class JevAuthInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .header(name = "Authorization", value = "Bearer ${BuildConfig.JEV_API_KEY}")
            .build()

        return chain.proceed(newRequest)
    }
}
