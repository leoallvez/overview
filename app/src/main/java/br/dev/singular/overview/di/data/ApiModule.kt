package br.dev.singular.overview.di.data

import br.dev.singular.overview.data.BuildConfig
import br.dev.singular.overview.data.network.ApiService
import br.dev.singular.overview.data.network.interceptor.ApiKeyInterceptor
import br.dev.singular.overview.data.network.interceptor.LocaleInterceptor
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter.Factory
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@OptIn(ExperimentalSerializationApi::class)
class ApiModule {

    @Singleton
    @Provides
    fun provideApiService(
        apiKeyInterceptor: ApiKeyInterceptor,
        localeInterceptor: LocaleInterceptor
    ): ApiService = buildService(apiKeyInterceptor, localeInterceptor).create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideJson(): Json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
        namingStrategy = JsonNamingStrategy.SnakeCase
    }

    private fun buildService(
        apiKeyInterceptor: ApiKeyInterceptor,
        localeInterceptor: LocaleInterceptor
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(buildConverterFactory())
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .client(buildOkHttpClient(apiKeyInterceptor, localeInterceptor))
            .build()
    }

    private fun buildConverterFactory(): Factory {
        return provideJson().asConverterFactory("application/json".toMediaType())
    }

    private fun buildOkHttpClient(
        apiKeyInterceptor: ApiKeyInterceptor,
        localeInterceptor: LocaleInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(buildHttpLoggingInterceptor())
            addInterceptor(apiKeyInterceptor)
            addInterceptor(localeInterceptor)
        }.build()
    }

    private fun buildHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}
