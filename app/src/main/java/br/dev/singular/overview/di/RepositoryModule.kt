package br.dev.singular.overview.di

import br.dev.singular.overview.data.repository.genre.GenreRepository
import br.dev.singular.overview.data.repository.genre.IGenreRepository
import br.dev.singular.overview.data.repository.media.remote.MediaPagingRepository
import br.dev.singular.overview.data.repository.media.remote.MediaRepository
import br.dev.singular.overview.data.repository.media.remote.MediaSearchPagingRepository
import br.dev.singular.overview.data.repository.media.remote.interfaces.IMediaPagingRepository
import br.dev.singular.overview.data.repository.media.remote.interfaces.IMediaRepository
import br.dev.singular.overview.data.repository.media.remote.interfaces.IMediaSearchPagingRepository
import br.dev.singular.overview.data.repository.mediatype.IMediaTypeRepository
import br.dev.singular.overview.data.repository.mediatype.MediaTypeRepository
import br.dev.singular.overview.data.repository.streaming.IStreamingRepository
import br.dev.singular.overview.data.repository.streaming.StreamingRepository
import br.dev.singular.overview.data.repository.streaming.selected.ISelectedStreamingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMediaRepository(
        repository: MediaRepository
    ): IMediaRepository

    @Binds
    abstract fun bindMediaPagingRepository(
        repository: MediaPagingRepository
    ): IMediaPagingRepository

    @Binds
    abstract fun bindMediaSearchPagingRepository(
        repository: MediaSearchPagingRepository
    ): IMediaSearchPagingRepository

    @Binds
    abstract fun bindStreamingRepository(
        repository: StreamingRepository
    ): IStreamingRepository

    @Binds
    abstract fun bindSelectedStreamingRepository(
        repository: StreamingRepository
    ): ISelectedStreamingRepository

    @Binds
    abstract fun bindGenreRepository(
        repository: GenreRepository
    ): IGenreRepository

    @Binds
    abstract fun bindMediaTypeRepository(
        repository: MediaTypeRepository
    ): IMediaTypeRepository
}
