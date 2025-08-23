package br.dev.singular.overview.di.data.local

import br.dev.singular.overview.data.local.source.IMediaRouteLocalDataSource
import br.dev.singular.overview.data.local.source.ISuggestionLocalDataSource
import br.dev.singular.overview.data.local.source.MediaRouteLocalDataSource
import br.dev.singular.overview.data.local.source.SuggestionLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    abstract fun bindSuggestionLocalDataSource(
        source: SuggestionLocalDataSource
    ): ISuggestionLocalDataSource

    @Binds
    abstract fun bindMediaRouteLocalDataSource(
        source: MediaRouteLocalDataSource
    ): IMediaRouteLocalDataSource

}
