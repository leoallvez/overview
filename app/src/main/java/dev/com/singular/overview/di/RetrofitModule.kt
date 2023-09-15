package dev.com.singular.overview.di

import br.dev.singular.overview.BuildConfig
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.com.singular.overview.data.api.ApiLocale
import dev.com.singular.overview.data.api.ApiService
import dev.com.singular.overview.data.api.IApiLocale
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
    fun provideApiClient(): ApiService {
        val retrofit = buildRetrofit()
        return retrofit.create(ApiService::class.java)
    }

    private fun buildRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .client(buildOkHttpClient())
            .build()
    }

    private fun buildOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
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
