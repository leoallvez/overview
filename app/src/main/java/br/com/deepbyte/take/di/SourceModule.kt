package br.com.deepbyte.take.di

import br.com.deepbyte.take.data.source.discover.DiscoverRemoteDataSource
import br.com.deepbyte.take.data.source.discover.IDiscoverRemoteDataSource
import br.com.deepbyte.take.data.source.media.IMediaRemoteDataSource
import br.com.deepbyte.take.data.source.media.MediaRemoteDataSource
import br.com.deepbyte.take.data.source.person.IPersonRemoteDataSource
import br.com.deepbyte.take.data.source.person.PersonRemoteDataSource
import br.com.deepbyte.take.data.source.provider.IProviderRemoteDataSource
import br.com.deepbyte.take.data.source.provider.ProviderRemoteDataSource
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
}
