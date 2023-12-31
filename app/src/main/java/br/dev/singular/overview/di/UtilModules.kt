package br.dev.singular.overview.di

import androidx.lifecycle.LiveData
import br.dev.singular.overview.util.ConnectivityLiveData
import br.dev.singular.overview.util.IJsonFileReader
import br.dev.singular.overview.util.JsonFileReader
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UtilModule {

    @Binds
    abstract fun bindJsonFileReader(
        jsonFileReader: JsonFileReader
    ): IJsonFileReader

    @IsOnline
    @Singleton
    @Binds
    abstract fun bindConnectivityLiveData(
        connectivityLiveData: ConnectivityLiveData
    ): LiveData<Boolean>
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class IsOnline
