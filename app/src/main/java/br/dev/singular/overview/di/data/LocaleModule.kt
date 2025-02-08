package br.dev.singular.overview.di.data

import br.dev.singular.overview.data.network.ILocaleProvider
import br.dev.singular.overview.data.network.LocaleProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocaleModule {
    @Binds
    abstract fun bindLocaleProvider(locale: LocaleProvider): ILocaleProvider
}
