package io.github.leoallvez.take.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.leoallvez.firebase.RemoteSource
import io.github.leoallvez.take.experiment.AbExperiment
import io.github.leoallvez.take.experiment.DisplayAdsExperiment
import io.github.leoallvez.take.BuildConfig
import io.github.leoallvez.take.data.model.*
import io.github.leoallvez.take.experiment.ListSetupExperiment
import io.github.leoallvez.take.util.IJsonFileReader

@Module
@InstallIn(SingletonComponent::class)
class AbExperimentModule {

    @AbDisplayAds
    @Provides
    fun providerDisplayAdsExperiment(
        remote: RemoteSource
    ): AbExperiment<Boolean> {
        return DisplayAdsExperiment(
            localPermission = BuildConfig.ADS_ARE_VISIBLES,
            remoteSource = remote
        )
    }

    @AbSuggestions
    @Provides
    fun providerListSetupExperiment(
        jsonFileReader: IJsonFileReader,
        remote: RemoteSource
    ): AbExperiment<List<Suggestion>> {
        return ListSetupExperiment(
            jsonFileReader = jsonFileReader,
            remoteSource = remote
        )
    }
}
