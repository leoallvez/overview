package dev.com.singular.overview.di

import dev.com.singular.overview.data.source.genre.GenreRemoteDataSource
import dev.com.singular.overview.data.source.genre.IGenreRemoteDataSource
import dev.com.singular.overview.data.source.person.IPersonRemoteDataSource
import dev.com.singular.overview.data.source.person.PersonRemoteDataSource
import dev.com.singular.overview.data.source.streaming.IStreamingRemoteDataSource
import dev.com.singular.overview.data.source.streaming.StreamingRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.com.singular.overview.data.model.media.Movie
import dev.com.singular.overview.data.model.media.TvShow
import dev.com.singular.overview.data.source.media.remote.IMediaDiscoverRemoteDataSource
import dev.com.singular.overview.data.source.media.remote.IMediaRemoteDataSource
import dev.com.singular.overview.data.source.media.remote.MovieRemoteDataSource
import dev.com.singular.overview.data.source.media.remote.TvShowRemoteDataSource

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
}
