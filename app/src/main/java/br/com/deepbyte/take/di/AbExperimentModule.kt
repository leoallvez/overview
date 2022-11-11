package br.com.deepbyte.take.di

import br.com.deepbyte.take.BuildConfig
import br.com.deepbyte.take.abtest.AbTest
import br.com.deepbyte.take.abtest.DisplayAdsAbTest
import br.com.deepbyte.take.abtest.SuggestionAbTest
import br.com.deepbyte.take.data.model.Suggestion
import br.com.deepbyte.take.util.IJsonFileReader
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
    ): AbTest<Boolean> {
        return DisplayAdsAbTest(
            _localPermission = BuildConfig.ADS_ARE_VISIBLES,
            _remoteSource = remote
        )
    }

    @AbSuggestions
    @Provides
    fun providerListSetupExperiment(
        jsonFileReader: IJsonFileReader,
        remote: RemoteSource
    ): AbTest<List<Suggestion>> {
        return SuggestionAbTest(
            _jsonFileReader = jsonFileReader,
            _remoteSource = remote
        )
    }
}
