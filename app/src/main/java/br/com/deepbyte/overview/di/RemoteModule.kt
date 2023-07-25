package br.com.deepbyte.overview.di

import br.com.deepbyte.overview.BuildConfig.ADS_ARE_VISIBLES
import br.com.deepbyte.overview.abtesting.RemoteConfig
import br.com.deepbyte.overview.abtesting.DisplayAdsRemoteConfig
import br.com.deepbyte.overview.abtesting.StreamingRemoteConfig
import br.com.deepbyte.overview.abtesting.StreamingV2RemoteConfig
import br.com.deepbyte.overview.abtesting.SuggestionRemoteConfig
import br.com.deepbyte.overview.data.model.Suggestion
import br.com.deepbyte.overview.data.model.provider.Streaming
import br.com.deepbyte.overview.data.model.provider.StreamingConfig
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

    @SuggestionsRemote
    @Provides
    fun providerSuggestionRemote(
        jsonFileReader: IJsonFileReader,
        remoteSource: RemoteSource
    ): RemoteConfig<List<Suggestion>> {
        return SuggestionRemoteConfig(jsonFileReader, remoteSource)
    }

    @StreamingsRemote
    @Provides
    fun providerStreamingRemote(
        jsonFileReader: IJsonFileReader,
        remoteSource: RemoteSource
    ): RemoteConfig<List<Streaming>> {
        return StreamingRemoteConfig(jsonFileReader, remoteSource)
    }

    @StreamingsRemoteV2
    @Provides
    fun providerStreamingV2Remote(
        remoteSource: RemoteSource
    ): RemoteConfig<StreamingConfig?> {
        return StreamingV2RemoteConfig(remoteSource)
    }
}
