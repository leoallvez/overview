package br.dev.singular.overview.di.app

import br.dev.singular.overview.data.util.ConnectivityMonitor
import br.dev.singular.overview.data.util.IConnectivityMonitor
import br.dev.singular.overview.data.util.IJsonFileReaderProvider
import br.dev.singular.overview.data.util.JsonFileReaderProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UtilModule {

    @Binds
    abstract fun bindJsonFileReaderProvider(
        jsonFileReader: JsonFileReaderProvider
    ): IJsonFileReaderProvider

    @Singleton
    @Binds
    abstract fun bindConnectivityMonitor(
        connectivityMonitor: ConnectivityMonitor
    ): IConnectivityMonitor
}
