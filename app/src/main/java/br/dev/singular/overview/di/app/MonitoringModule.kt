package br.dev.singular.overview.di.app

import br.dev.singular.overview.monitoring.CrashlyticsSource
import br.dev.singular.overview.monitoring.CrashlyticsWrapper
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MonitoringModule {

    @Singleton
    @Provides
    fun provideCrashlyticsSource(): CrashlyticsSource {
        return CrashlyticsWrapper(FirebaseCrashlytics.getInstance())
    }
}
