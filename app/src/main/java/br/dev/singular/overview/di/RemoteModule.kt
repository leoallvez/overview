package br.dev.singular.overview.di

import br.dev.singular.overview.BuildConfig.ADS_ARE_VISIBLES
import br.dev.singular.overview.data.api.ApiLocale
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.remote.DisplayAdsRemoteConfig
import br.dev.singular.overview.remote.RemoteConfig
import br.dev.singular.overview.remote.StreamingsRemoteConfig
import br.dev.singular.overview.util.IJsonFileReader
import io.github.leoallvez.firebase.RemoteSource

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @ShowAds
    @Provides
    fun providerDisplayAdsRemote(
        remoteSource: RemoteSource
    ): Boolean {
        return DisplayAdsRemoteConfig(
            _localPermission = ADS_ARE_VISIBLES,
            _remoteSource = remoteSource
        ).execute()
    }

    @StreamingsRemote
    @Provides
    fun providerStreamingsRemote(
        apiLocale: ApiLocale,
        remoteSource: RemoteSource,
        jsonFileReader: IJsonFileReader
    ): RemoteConfig<List<StreamingEntity>> {
        val region = apiLocale.region
        return StreamingsRemoteConfig(region, remoteSource, jsonFileReader)
    }
}
