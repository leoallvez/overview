package br.dev.singular.overview.di.data

import br.dev.singular.overview.BuildConfig
import br.dev.singular.overview.data.remoteconfig.DisplayAdsRemoteConfig
import br.dev.singular.overview.data.remoteconfig.DisplayAiPredictionRemoteConfig
import br.dev.singular.overview.data.remoteconfig.IRemoteConfigProvider
import br.dev.singular.overview.data.remoteconfig.RemoteConfig
import br.dev.singular.overview.data.remoteconfig.RemoteConfigWrapper
import br.dev.singular.overview.navigation.FeatureFlags
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DisplayAds

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DisplayAiPrediction

@Module
@InstallIn(SingletonComponent::class)
class RemoteConfigModule {

    @Singleton
    @Provides
    fun provideRemoteConfigProvider(): IRemoteConfigProvider {
        return RemoteConfigWrapper(Firebase.remoteConfig)
    }

    @Provides
    @DisplayAds
    fun providerDisplayAdsRemote(
        remoteSource: IRemoteConfigProvider
    ): RemoteConfig<Boolean> {
        return DisplayAdsRemoteConfig(
            _localOverride = BuildConfig.LOCAL_DISPLAY_ADS,
            _remoteSource = remoteSource
        )
    }

    @Provides
    @DisplayAiPrediction
    fun providerDisplayAiPredictionRemote(
        remoteSource: IRemoteConfigProvider
    ): RemoteConfig<Boolean> {
        return DisplayAiPredictionRemoteConfig(
            _localOverride = BuildConfig.LOCAL_DISPLAY_AI_PREDICTION,
            _remoteSource = remoteSource
        )
    }

    @Provides
    fun provideFeatureFlags(
        @DisplayAds displayAds: RemoteConfig<Boolean>,
        @DisplayAiPrediction displayAiPrediction: RemoteConfig<Boolean>
    ): FeatureFlags {
        return FeatureFlags(
            showAds = displayAds.execute(),
            showAiPrediction = displayAiPrediction.execute()
        )
    }
}
