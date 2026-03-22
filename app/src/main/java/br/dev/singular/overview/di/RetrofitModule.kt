package br.dev.singular.overview.di

import br.dev.singular.overview.data.BuildConfig
import br.dev.singular.overview.data.api.ApiLocale
import br.dev.singular.overview.data.api.ApiService
import br.dev.singular.overview.data.api.IApiLocale
import br.dev.singular.overview.data.network.interceptor.ApiKeyInterceptor
import br.dev.singular.overview.data.network.interceptor.LocaleInterceptor
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Singleton
    @Provides
    fun provideApiClient(
        apiKeyInterceptor: ApiKeyInterceptor,
        localeInterceptor: LocaleInterceptor
    ): ApiService {
        val retrofit = buildRetrofit(apiKeyInterceptor, localeInterceptor)
        return retrofit.create(ApiService::class.java)
    }

    private fun buildRetrofit(
        apiKeyInterceptor: ApiKeyInterceptor,
        localeInterceptor: LocaleInterceptor
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .client(buildOkHttpClient(apiKeyInterceptor, localeInterceptor))
            .build()
    }

    private fun buildOkHttpClient(
        apiKeyInterceptor: ApiKeyInterceptor,
        localeInterceptor: LocaleInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(apiKeyInterceptor)
            addInterceptor(localeInterceptor)
            addInterceptor(buildHttpLoggingInterceptor())
        }.build()
    }

    private fun buildHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {
    @Binds
    abstract fun bindApiLocale(locale: ApiLocale): IApiLocale
}
