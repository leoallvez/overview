package br.dev.singular.overview.di.data.network

import br.dev.singular.overview.data.network.source.CatalogRemoteDataSource
import br.dev.singular.overview.data.network.source.GenreRemoteDataSource
import br.dev.singular.overview.data.network.source.ICatalogRemoteDataSource
import br.dev.singular.overview.data.network.source.IGenreRemoteDataSource
import br.dev.singular.overview.data.network.source.IMediaRemoteDataSource
import br.dev.singular.overview.data.network.source.IPersonDetailsRemoteDataSource
import br.dev.singular.overview.data.network.source.ISuggestionRemoteDataSource
import br.dev.singular.overview.data.network.source.IVideoRemoteDataSource
import br.dev.singular.overview.data.network.source.MediaRemoteDataSource
import br.dev.singular.overview.data.network.source.PersonDetailsRemoteDataSource
import br.dev.singular.overview.data.network.source.SuggestionRemoteDataSource
import br.dev.singular.overview.data.network.source.VideoRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    abstract fun bindMediaRemoteDataSource(
        source: MediaRemoteDataSource
    ): IMediaRemoteDataSource

    @Binds
    abstract fun bindSuggestionRemoteDataSource(
        source: SuggestionRemoteDataSource
    ): ISuggestionRemoteDataSource

    @Binds
    abstract fun bindPersonDetailsRemoteDataSource(
        source: PersonDetailsRemoteDataSource
    ): IPersonDetailsRemoteDataSource

    @Binds
    abstract fun bindCatalogRemoteDataSource(
        source: CatalogRemoteDataSource
    ): ICatalogRemoteDataSource

    @Binds
    abstract fun bindGenreRemoteDataSource(
        source: GenreRemoteDataSource
    ): IGenreRemoteDataSource

    @Binds
    abstract fun bindVideoRemoteDataSource(
        source: VideoRemoteDataSource
    ): IVideoRemoteDataSource
}
