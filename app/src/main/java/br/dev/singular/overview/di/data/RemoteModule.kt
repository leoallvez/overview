package br.dev.singular.overview.di.data

import br.dev.singular.overview.BuildConfig
import br.dev.singular.overview.data.remoteconfig.DisplayAdsRemoteConfig
import br.dev.singular.overview.data.remoteconfig.IRemoteConfigProvider
import br.dev.singular.overview.data.remoteconfig.RemoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @Provides
    fun providerDisplayAdsRemote(
        remoteSource: IRemoteConfigProvider
    ): RemoteConfig<Boolean> {
        return DisplayAdsRemoteConfig(
            _localPermission = BuildConfig.DEBUG,
            _remoteSource = remoteSource
        )
    }
}
