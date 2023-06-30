package br.com.deepbyte.overview.di

import br.com.deepbyte.overview.data.repository.genre.GenreRepository
import br.com.deepbyte.overview.data.repository.genre.IGenreRepository
import br.com.deepbyte.overview.data.repository.media.MediaItemRepository
import br.com.deepbyte.overview.data.repository.media.MediaPagingRepository
import br.com.deepbyte.overview.data.repository.media.interfaces.IMediaItemRepository
import br.com.deepbyte.overview.data.repository.media.interfaces.IMediaPagingRepository
import br.com.deepbyte.overview.data.repository.mediatype.IMediaTypeRepository
import br.com.deepbyte.overview.data.repository.mediatype.MediaTypeRepository
import br.com.deepbyte.overview.data.repository.person.IPersonRepository
import br.com.deepbyte.overview.data.repository.person.PersonRepository
import br.com.deepbyte.overview.data.repository.search.ISearchRepository
import br.com.deepbyte.overview.data.repository.search.SearchRepository
import br.com.deepbyte.overview.data.repository.streaming.IStreamingRepository
import br.com.deepbyte.overview.data.repository.streaming.StreamingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun binMediaItemRepository(
        repository: MediaItemRepository
    ): IMediaItemRepository

    @Binds
    abstract fun bindMediaPagingRepository(
        repository: MediaPagingRepository
    ): IMediaPagingRepository

    @Binds
    abstract fun binPersonRepository(
        repository: PersonRepository
    ): IPersonRepository

    @Binds
    abstract fun binSearchRepository(
        repository: SearchRepository
    ): ISearchRepository

    @Binds
    abstract fun binStreamingRepository(
        repository: StreamingRepository
    ): IStreamingRepository

    @Binds
    abstract fun binGenreRepository(
        repository: GenreRepository
    ): IGenreRepository

    @Binds
    abstract fun binMediaTypeRepository(
        repository: MediaTypeRepository
    ): IMediaTypeRepository
}
