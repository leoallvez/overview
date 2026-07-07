package br.dev.singular.overview.di.presentation

import br.dev.singular.overview.presentation.ui.screens.media.IMediaDetailsDelegate
import br.dev.singular.overview.presentation.ui.screens.media.MediaDetailsDelegate
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DelegateModule {

    @Binds
    @Singleton
    abstract fun bindMediaDetailsDelegate(
        impl: MediaDetailsDelegate
    ): IMediaDetailsDelegate

}
