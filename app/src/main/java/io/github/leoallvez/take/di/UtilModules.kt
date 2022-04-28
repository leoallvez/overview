package io.github.leoallvez.take.di

import androidx.lifecycle.LiveData
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.leoallvez.take.util.ConnectivityLiveData
import io.github.leoallvez.take.util.IJsonFileReader
import io.github.leoallvez.take.util.JsonFileReader
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
