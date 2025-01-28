package br.dev.singular.overview.di

import br.dev.singular.overview.data.model.media.Movie
import br.dev.singular.overview.data.model.media.TvShow
import br.dev.singular.overview.data.source.genre.GenreRemoteDataSource
import br.dev.singular.overview.data.source.genre.IGenreRemoteDataSource
import br.dev.singular.overview.data.source.media.remote.IMediaDiscoverRemoteDataSource
import br.dev.singular.overview.data.source.media.remote.IMediaRemoteDataSource
import br.dev.singular.overview.data.source.media.remote.MovieRemoteDataSource
import br.dev.singular.overview.data.source.media.remote.TvShowRemoteDataSource
import br.dev.singular.overview.data.source.person.IPersonRemoteDataSource
import br.dev.singular.overview.data.source.person.PersonRemoteDataSource
import br.dev.singular.overview.data.source.streaming.IStreamingRemoteDataSource
import br.dev.singular.overview.data.source.streaming.StreamingRemoteDataSource
import br.dev.singular.overview.data.source.trailer.ITrailerRemoteDataSource
import br.dev.singular.overview.data.source.trailer.TrailerRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SourceModule {

    @Binds
    abstract fun bindPersonRemoteDataSource(
        source: PersonRemoteDataSource
    ): IPersonRemoteDataSource

    @Binds
    abstract fun bindMovieRemoteDataSource(
        source: MovieRemoteDataSource
    ): IMediaRemoteDataSource<Movie>

    @Binds
    abstract fun bindTvShowRemoteDataSource(
        source: TvShowRemoteDataSource
    ): IMediaRemoteDataSource<TvShow>

    @Binds
    abstract fun bindMediaDiscoverRemoteDataSource(
        source: TvShowRemoteDataSource
    ): IMediaDiscoverRemoteDataSource<TvShow>

    @Binds
    abstract fun bindStreamingRemoteDataSource(
        source: StreamingRemoteDataSource
    ): IStreamingRemoteDataSource

    @Binds
    abstract fun bindGenreDataSource(
        source: GenreRemoteDataSource
    ): IGenreRemoteDataSource

    @Binds
    abstract fun bindTrailerRemoteDataSource(
        source: TrailerRemoteDataSource
    ): ITrailerRemoteDataSource
}
