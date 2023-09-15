package dev.com.singular.overview.di

import br.dev.singular.overview.BuildConfig.ADS_ARE_VISIBLES
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.com.singular.overview.data.api.ApiLocale
import dev.com.singular.overview.data.model.provider.StreamingEntity
import dev.com.singular.overview.remote.DisplayAdsRemoteConfig
import dev.com.singular.overview.remote.RemoteConfig
import dev.com.singular.overview.remote.StreamingsRemoteConfig
import dev.com.singular.overview.util.IJsonFileReader
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
