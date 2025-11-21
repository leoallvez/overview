package br.dev.singular.overview.di.data.network

import br.dev.singular.overview.data.network.source.IMediaRemoteDataSource
import br.dev.singular.overview.data.network.source.IPersonRemoteDataSource
import br.dev.singular.overview.data.network.source.ISuggestionRemoteDataSource
import br.dev.singular.overview.data.network.source.MediaRemoteDataSource
import br.dev.singular.overview.data.network.source.PersonRemoteDataSource
import br.dev.singular.overview.data.network.source.SuggestionRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    abstract fun bindMediaRemoteDataSource(
        source: MediaRemoteDataSource
    ): IMediaRemoteDataSource

    @Binds
    abstract fun bindSuggestionRemoteDataSource(
        source: SuggestionRemoteDataSource
    ): ISuggestionRemoteDataSource

    @Binds
    abstract fun bindPersonRemoteDataSource(
        source: PersonRemoteDataSource
    ): IPersonRemoteDataSource
}
