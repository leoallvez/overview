package br.com.deepbyte.overview.di

import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.source.genre.GenreRemoteDataSource
import br.com.deepbyte.overview.data.source.genre.IGenreRemoteDataSource
import br.com.deepbyte.overview.data.source.media.*
import br.com.deepbyte.overview.data.source.media.remote.*
import br.com.deepbyte.overview.data.source.media.v1.MediaRemoteDataSource
import br.com.deepbyte.overview.data.source.person.IPersonRemoteDataSource
import br.com.deepbyte.overview.data.source.person.PersonRemoteDataSource
import br.com.deepbyte.overview.data.source.streaming.IStreamingRemoteDataSource
import br.com.deepbyte.overview.data.source.streaming.StreamingRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import br.com.deepbyte.overview.data.source.media.v1.IMediaRemoteDataSource as IMediaRemoteDataSourceV1

@Module
@InstallIn(SingletonComponent::class)
abstract class SourceModule {

    @Binds
    abstract fun bindMediaRemoteDataSource(
        source: MediaRemoteDataSource
    ): IMediaRemoteDataSourceV1

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
    abstract fun bindStreamingRemoteDataSource(
        source: StreamingRemoteDataSource
    ): IStreamingRemoteDataSource

    @Binds
    abstract fun bindGenreDataSource(
        source: GenreRemoteDataSource
    ): IGenreRemoteDataSource
}
