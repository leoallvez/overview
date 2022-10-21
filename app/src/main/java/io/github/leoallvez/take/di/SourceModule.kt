package io.github.leoallvez.take.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.leoallvez.take.data.source.media_item.IMediaRemoteDataSource
import io.github.leoallvez.take.data.source.media_item.MediaRemoteDataSource
import io.github.leoallvez.take.data.source.person.IPersonRemoteDataSource
import io.github.leoallvez.take.data.source.person.PersonRemoteDataSource
import io.github.leoallvez.take.data.source.provider.IProviderRemoteDataSource
import io.github.leoallvez.take.data.source.provider.ProviderRemoteDataSource

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
    abstract fun bindProviderDataSource(
        source: ProviderRemoteDataSource
    ): IProviderRemoteDataSource
}
