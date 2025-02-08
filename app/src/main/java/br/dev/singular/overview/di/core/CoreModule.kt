package br.dev.singular.overview.di.core

import android.content.Context
import br.dev.singular.overview.AnalyticsTracker
import br.dev.singular.overview.IAnalyticsTracker
import br.dev.singular.overview.core.analytics.AnalyticsWrapper
import br.dev.singular.overview.core.crashlytics.CrashlyticsSource
import br.dev.singular.overview.core.crashlytics.CrashlyticsWrapper
import br.dev.singular.overview.core.remote.RemoteConfigProvider
import br.dev.singular.overview.core.remote.RemoteConfigWrapper
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideAnalyticsTracker(
        @ApplicationContext context: Context
    ): IAnalyticsTracker {
        val wrapper = AnalyticsWrapper(FirebaseAnalytics.getInstance(context))
        return AnalyticsTracker(wrapper)
    }

    @Singleton
    @Provides
    fun provideCrashlyticsSource(): CrashlyticsSource {
        return CrashlyticsWrapper(FirebaseCrashlytics.getInstance())
    }
}
