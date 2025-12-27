package br.dev.singular.overview.di

import br.dev.singular.overview.BuildConfig
import br.dev.singular.overview.core.remote.RemoteConfigProvider
import br.dev.singular.overview.remote.DisplayAdsRemoteConfig
import br.dev.singular.overview.remote.DisplayHighlightIconsRemoteConfig
import br.dev.singular.overview.remote.RemoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @Provides
    @DisplayAds
    fun providerDisplayAdsRemote(
        remoteSource: RemoteConfigProvider
    ): RemoteConfig<Boolean> {
        return DisplayAdsRemoteConfig(
            _localPermission = BuildConfig.DEBUG,
            _remoteSource = remoteSource
        )
    }

    @Provides
    @DisplayHighlightIcons
    fun providerDisplayHighlightIconsRemote(
        remoteSource: RemoteConfigProvider
    ): RemoteConfig<Boolean> {
        return DisplayHighlightIconsRemoteConfig(
            _remoteSource = remoteSource
        )
    }
}
