package dev.com.singular.overview.di

import dev.com.singular.overview.data.repository.streaming.IStreamingRepository
import dev.com.singular.overview.data.repository.streaming.StreamingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.com.singular.overview.data.repository.genre.GenreRepository
import dev.com.singular.overview.data.repository.genre.IGenreRepository
import dev.com.singular.overview.data.repository.media.MediaCacheRepository
import dev.com.singular.overview.data.repository.media.MediaPagingRepository
import dev.com.singular.overview.data.repository.media.MediaRepository
import dev.com.singular.overview.data.repository.media.interfaces.IMediaCacheRepository
import dev.com.singular.overview.data.repository.media.interfaces.IMediaPagingRepository
import dev.com.singular.overview.data.repository.media.interfaces.IMediaRepository
import dev.com.singular.overview.data.repository.mediatype.IMediaTypeRepository
import dev.com.singular.overview.data.repository.mediatype.MediaTypeRepository
import dev.com.singular.overview.data.repository.person.IPersonRepository
import dev.com.singular.overview.data.repository.person.PersonRepository
import dev.com.singular.overview.data.repository.search.ISearchPagingRepository
import dev.com.singular.overview.data.repository.search.SearchMediaPagingRepository

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
    abstract fun bindPersonRepository(
        repository: PersonRepository
    ): IPersonRepository

    @Binds
    abstract fun bindSearchPagingRepository(
        repository: SearchMediaPagingRepository
    ): ISearchPagingRepository

    @Binds
    abstract fun bindStreamingRepository(
        repository: StreamingRepository
    ): IStreamingRepository

    @Binds
    abstract fun bindGenreRepository(
        repository: GenreRepository
    ): IGenreRepository

    @Binds
    abstract fun bindMediaTypeRepository(
        repository: MediaTypeRepository
    ): IMediaTypeRepository

    @Binds
    abstract fun bindMediaCacheRepository(
        repository: MediaCacheRepository
    ): IMediaCacheRepository
}
