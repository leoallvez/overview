package br.com.deepbyte.overview.di

import br.com.deepbyte.overview.BuildConfig.ADS_ARE_VISIBLES
import br.com.deepbyte.overview.data.api.ApiLocale
import br.com.deepbyte.overview.data.model.provider.StreamingEntity
import br.com.deepbyte.overview.remote.DisplayAdsRemoteConfig
import br.com.deepbyte.overview.remote.RemoteConfig
import br.com.deepbyte.overview.remote.StreamingsRemoteConfig
import br.com.deepbyte.overview.util.IJsonFileReader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
