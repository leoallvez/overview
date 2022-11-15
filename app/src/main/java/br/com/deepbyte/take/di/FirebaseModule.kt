package br.com.deepbyte.take.di

import android.content.Context
import br.com.deepbyte.take.AnalyticsTracker
import br.com.deepbyte.take.IAnalyticsTracker
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.leoallvez.firebase.RemoteSource
import io.github.leoallvez.firebase.RemoteConfigWrapper
import io.github.leoallvez.firebase.AnalyticsSource
import io.github.leoallvez.firebase.AnalyticsWrapper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProvidesFirebaseModule {

    @Singleton
    @Provides
    fun provideRemoteSource(): RemoteSource {
        val remote = Firebase.remoteConfig
        return RemoteConfigWrapper(remote)
            .apply { start() }
    }

    @Singleton
    @Provides
    fun provideAnalyticsSource(
        @ApplicationContext context: Context
    ): AnalyticsSource {
        return AnalyticsWrapper(FirebaseAnalytics.getInstance(context))
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class BindsFirebaseModule {
    @Binds
    abstract fun bindAnalyticsTracker(
        locale: AnalyticsTracker
    ): IAnalyticsTracker
}
