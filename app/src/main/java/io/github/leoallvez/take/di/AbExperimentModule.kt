package io.github.leoallvez.take.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.leoallvez.firebase.RemoteSource
import io.github.leoallvez.take.experiment.AbExperiment
import io.github.leoallvez.take.experiment.DisplayAdsExperiment
import io.github.leoallvez.take.BuildConfig

@Module
@InstallIn(SingletonComponent::class)
class AbExperimentModule {

    @Provides
    fun providerDisplayAdsExperiment(
        remote: RemoteSource
    ): AbExperiment<Boolean> {
        return DisplayAdsExperiment(
            localPermission = BuildConfig.ADS_ARE_VISIBLES,
            remoteSource = remote
        )
    }
}
