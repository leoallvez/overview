package br.com.deepbyte.overview.di

import br.com.deepbyte.overview.BuildConfig.ADS_ARE_VISIBLES
import br.com.deepbyte.overview.abtesting.AbTesting
import br.com.deepbyte.overview.abtesting.DisplayAdsAbTesting
import br.com.deepbyte.overview.abtesting.StreamingAbTesting
import br.com.deepbyte.overview.abtesting.SuggestionAbTesting
import br.com.deepbyte.overview.data.model.Suggestion
import br.com.deepbyte.overview.data.model.provider.Streaming
import br.com.deepbyte.overview.util.IJsonFileReader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.leoallvez.firebase.RemoteSource

@Module
@InstallIn(SingletonComponent::class)
class AbExperimentModule {

    @ShowAds
    @Provides
    fun providerShowAdsExperiment(
        remoteSource: RemoteSource
    ): Boolean {
        return DisplayAdsAbTesting(
            _localPermission = ADS_ARE_VISIBLES,
            _remoteSource = remoteSource
        ).execute()
    }

    @AbSuggestions
    @Provides
    fun providerAbSuggestions(
        jsonFileReader: IJsonFileReader,
        remoteSource: RemoteSource
    ): AbTesting<List<Suggestion>> {
        return SuggestionAbTesting(jsonFileReader, remoteSource)
    }

    @AbStreamings
    @Provides
    fun providerAbStreamings(
        jsonFileReader: IJsonFileReader,
        remoteSource: RemoteSource
    ): AbTesting<List<Streaming>> {
        return StreamingAbTesting(jsonFileReader, remoteSource)
    }
}
