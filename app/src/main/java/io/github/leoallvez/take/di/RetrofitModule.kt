package io.github.leoallvez.take.di

import dagger.Provides
import io.github.leoallvez.take.BuildConfig
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.leoallvez.take.data.api.ApiLocale
import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.api.IApiLocale

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
            .build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {
    @Binds
    abstract fun bindApiLocale(locale: ApiLocale): IApiLocale
}

