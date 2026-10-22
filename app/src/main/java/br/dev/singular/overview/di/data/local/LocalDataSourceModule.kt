package br.dev.singular.overview.di.data.local

import br.dev.singular.overview.data.local.source.CatalogLocalDataSource
import br.dev.singular.overview.data.local.source.GenreLocalDataSource
import br.dev.singular.overview.data.local.source.ICatalogLocalDataSource
import br.dev.singular.overview.data.local.source.IGenreLocalDataSource
import br.dev.singular.overview.data.local.source.IMediaRouteLocalDataSource
import br.dev.singular.overview.data.local.source.ISuggestionLocalDataSource
import br.dev.singular.overview.data.local.source.IPendingFavoriteLocalDataSource
import br.dev.singular.overview.data.local.source.MediaRouteLocalDataSource
import br.dev.singular.overview.data.local.source.PendingFavoriteLocalDataSource
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

    @Binds
    abstract fun bindCatalogLocalDataSource(
        source: CatalogLocalDataSource
    ): ICatalogLocalDataSource

    @Binds
    abstract fun bindGenreLocalDataSource(
        source: GenreLocalDataSource
    ): IGenreLocalDataSource

    @Binds
    abstract fun bindPendingFavoriteLocalDataSource(
        source: PendingFavoriteLocalDataSource
    ): IPendingFavoriteLocalDataSource
}
