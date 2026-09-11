package br.dev.singular.overview.di.data

import br.dev.singular.overview.data.remote.config.IRemoteConfigProvider
import br.dev.singular.overview.data.remote.config.RemoteConfigWrapper
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteConfigModule {

    @Singleton
    @Provides
    fun provideRemoteConfigProvider(): IRemoteConfigProvider {
        return RemoteConfigWrapper(Firebase.remoteConfig)
    }
}
