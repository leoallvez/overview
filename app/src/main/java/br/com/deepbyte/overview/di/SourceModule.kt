package br.com.deepbyte.overview.di

import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.source.discover.DiscoverRemoteDataSource
import br.com.deepbyte.overview.data.source.discover.IDiscoverRemoteDataSource
import br.com.deepbyte.overview.data.source.media.*
import br.com.deepbyte.overview.data.source.person.IPersonRemoteDataSource
import br.com.deepbyte.overview.data.source.person.PersonRemoteDataSource
import br.com.deepbyte.overview.data.source.provider.IProviderRemoteDataSource
import br.com.deepbyte.overview.data.source.provider.ProviderRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SourceModule {

    @Binds
    abstract fun bindMediaRemoteDataSource(
        source: MediaRemoteDataSource
    ): IMediaRemoteDataSource

    @Binds
    abstract fun bindPersonRemoteDataSource(
        source: PersonRemoteDataSource
    ): IPersonRemoteDataSource

    @Binds
    abstract fun bindProviderRemoteDataSource(
        source: ProviderRemoteDataSource
    ): IProviderRemoteDataSource

    @Binds
    abstract fun bindDiscoverRemoteDataSource(
        source: DiscoverRemoteDataSource
    ): IDiscoverRemoteDataSource

    @Binds
    abstract fun bindMovieRemoteDataSource(
        source: MovieRemoteDataSource
    ): IMediaRemoteDataSourceV2<Movie>

    @Binds
    abstract fun bindTvShowRemoteDataSource(
        source: TvShowRemoteDataSource
    ): IMediaRemoteDataSourceV2<TvShow>
}
