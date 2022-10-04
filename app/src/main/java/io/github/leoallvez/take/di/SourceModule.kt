package io.github.leoallvez.take.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.leoallvez.take.data.source.mediaitem.IMediaRemoteDataSource
import io.github.leoallvez.take.data.source.mediaitem.MediaRemoteDataSource

@Module
@InstallIn(SingletonComponent::class)
abstract class SourceModule {

    @Binds
    abstract fun bindMediaRemoteDataSource(
        source: MediaRemoteDataSource
    ): IMediaRemoteDataSource
}
