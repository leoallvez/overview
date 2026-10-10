package br.dev.singular.overview.di.data

import br.dev.singular.overview.data.BuildConfig
import br.dev.singular.overview.data.network.TmdbApiService
import br.dev.singular.overview.data.network.JevApiService
import br.dev.singular.overview.data.network.interceptor.TmdbApiKeyInterceptor
import br.dev.singular.overview.data.network.interceptor.JevAuthInterceptor
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
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
@OptIn(ExperimentalSerializationApi::class)
class ApiModule {

    @Singleton
    @Provides
    fun provideTmdbApiService(
        apiKeyInterceptor: TmdbApiKeyInterceptor,
        localeInterceptor: LocaleInterceptor
    ): TmdbApiService {
        return builder(BuildConfig.TMDB_API_URL)
            .client(buildOkHttpClient(apiKeyInterceptor, localeInterceptor))
            .build()
            .create(TmdbApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideJevApiService(
        jevAuthInterceptor: JevAuthInterceptor
    ): JevApiService {
        val client = OkHttpClient.Builder().apply {
            addInterceptor(buildHttpLoggingInterceptor())
            addInterceptor(jevAuthInterceptor)
        }.build()
        return builder(BuildConfig.JEV_API_URL)
            .client(client)
            .build()
            .create(JevApiService::class.java)
    }

    private fun builder(baseUrl: String): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(buildConverterFactory())
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
    }

    @Singleton
    @Provides
    fun provideJson(): Json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
        namingStrategy = JsonNamingStrategy.SnakeCase
    }

    private fun buildConverterFactory(): Factory {
        return provideJson().asConverterFactory("application/json".toMediaType())
    }

    private fun buildOkHttpClient(
        apiKeyInterceptor: TmdbApiKeyInterceptor,
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
        interceptor.redactHeader("Authorization")
        return interceptor
    }
}
