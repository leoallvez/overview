package br.dev.singular.overview.di.core

import br.dev.singular.overview.core.crashlytics.CrashlyticsSource
import br.dev.singular.overview.core.crashlytics.CrashlyticsWrapper
import br.dev.singular.overview.core.remote.RemoteConfigProvider
import br.dev.singular.overview.core.remote.RemoteConfigWrapper
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoreModule {

    @Singleton
    @Provides
    fun provideRemoteConfigProvider(): RemoteConfigProvider {
        return RemoteConfigWrapper(Firebase.remoteConfig)
    }

    @Singleton
    @Provides
    fun provideCrashlyticsSource(): CrashlyticsSource {
        return CrashlyticsWrapper(FirebaseCrashlytics.getInstance())
    }
}
