package br.dev.singular.overview.di

import br.dev.singular.overview.BuildConfig
import br.dev.singular.overview.core.remote.RemoteConfigProvider
import br.dev.singular.overview.data.api.ApiLocale
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.data.util.IJsonFileReaderProvider
import br.dev.singular.overview.remote.DisplayAdsRemoteConfig
import br.dev.singular.overview.remote.DisplayHighlightIconsRemoteConfig
import br.dev.singular.overview.remote.RemoteConfig
import br.dev.singular.overview.remote.StreamingRemoteConfig
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

    @Provides
    @StreamingList
    fun providerStreamingListRemote(
        apiLocale: ApiLocale,
        remoteSource: RemoteConfigProvider,
        jsonFileReader: IJsonFileReaderProvider
    ): RemoteConfig<List<StreamingEntity>> {
        val region = apiLocale.region
        return StreamingRemoteConfig(
            region,
            remoteSource,
            jsonFileReader
        )
    }
}
