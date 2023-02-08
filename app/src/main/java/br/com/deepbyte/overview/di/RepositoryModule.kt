package br.com.deepbyte.overview.di

import br.com.deepbyte.overview.data.repository.discover.DiscoverRepository
import br.com.deepbyte.overview.data.repository.discover.IDiscoverRepository
import br.com.deepbyte.overview.data.repository.media.IMediaRepository
import br.com.deepbyte.overview.data.repository.media.MediaRepository
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
    abstract fun binDiscoverRepository(
        repository: DiscoverRepository
    ): IDiscoverRepository

    @Binds
    abstract fun binMediaRepository(
        repository: MediaRepository
    ): IMediaRepository

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
}
