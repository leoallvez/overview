package io.github.leoallvez.take.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.leoallvez.firebase.RemoteSource
import io.github.leoallvez.take.abtest.AbTest
import io.github.leoallvez.take.abtest.DisplayAdsAbTest
import io.github.leoallvez.take.BuildConfig
import io.github.leoallvez.take.data.model.*
import io.github.leoallvez.take.abtest.SuggestionAbTest
import io.github.leoallvez.take.util.IJsonFileReader

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
