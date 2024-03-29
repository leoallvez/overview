package br.dev.singular.overview.di

import android.content.Context
import br.dev.singular.overview.AnalyticsTracker
import br.dev.singular.overview.IAnalyticsTracker
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.leoallvez.firebase.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    @Singleton
    @Provides
    fun provideRemoteSource(): RemoteSource {
        val remote = Firebase.remoteConfig
        return RemoteConfigWrapper(remote)
    }

    @Singleton
    @Provides
    fun provideAnalyticsTracker(
        @ApplicationContext context: Context
    ): IAnalyticsTracker {
        val wrapper = AnalyticsWrapper(FirebaseAnalytics.getInstance(context))
        return AnalyticsTracker(_source = wrapper)
    }

    @Singleton
    @Provides
    fun provideCrashlytics(): CrashlyticsSource {
        return CrashlyticsWrapper(FirebaseCrashlytics.getInstance())
    }
}
