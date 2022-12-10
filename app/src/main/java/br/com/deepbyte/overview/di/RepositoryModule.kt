package br.com.deepbyte.overview.di

import br.com.deepbyte.overview.data.repository.discover.DiscoverRepository
import br.com.deepbyte.overview.data.repository.discover.IDiscoverRepository
import br.com.deepbyte.overview.data.repository.media.IMediaRepository
import br.com.deepbyte.overview.data.repository.media.MediaRepository
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
}
