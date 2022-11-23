package br.com.deepbyte.overview.di

import br.com.deepbyte.overview.BuildConfig
import br.com.deepbyte.overview.abtesting.AbTesting
import br.com.deepbyte.overview.abtesting.DisplayAdsAbTesting
import br.com.deepbyte.overview.abtesting.SuggestionAbTesting
import br.com.deepbyte.overview.data.model.Suggestion
import br.com.deepbyte.overview.util.IJsonFileReader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.leoallvez.firebase.RemoteSource

@Module
@InstallIn(SingletonComponent::class)
class AbExperimentModule {

    @AbDisplayAds
    @Provides
    fun providerDisplayAdsExperiment(
        remote: RemoteSource
    ): AbTesting<Boolean> {
        return DisplayAdsAbTesting(
            _localPermission = BuildConfig.ADS_ARE_VISIBLES,
            _remoteSource = remote
        )
    }

    @AbSuggestions
    @Provides
    fun providerListSetupExperiment(
        jsonFileReader: IJsonFileReader,
        remote: RemoteSource
    ): AbTesting<List<Suggestion>> {
        return SuggestionAbTesting(
            _jsonFileReader = jsonFileReader,
            _remoteSource = remote
        )
    }
}
