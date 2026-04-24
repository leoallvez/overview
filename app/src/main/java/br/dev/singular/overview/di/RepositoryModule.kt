package br.dev.singular.overview.di

import br.dev.singular.overview.data.repository.media.remote.MediaRepository
import br.dev.singular.overview.data.repository.media.remote.MediaSearchPagingRepository
import br.dev.singular.overview.data.repository.media.remote.interfaces.IMediaRepository
import br.dev.singular.overview.data.repository.media.remote.interfaces.IMediaSearchPagingRepository
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
    abstract fun bindMediaSearchPagingRepository(
        repository: MediaSearchPagingRepository
    ): IMediaSearchPagingRepository

}
