package io.github.leoallvez.take.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.leoallvez.take.util.IJsonFileReader
import io.github.leoallvez.take.util.JsonFileReader

@Module
@InstallIn(SingletonComponent::class)
abstract class UtilModule {
    @Binds
    abstract fun bindJsonFileReader(
        jsonFileReader: JsonFileReader
    ): IJsonFileReader
}
