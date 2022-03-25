package io.github.leoallvez.take.di

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.leoallvez.firebase.RemoteConfigWrapper
import io.github.leoallvez.firebase.RemoteSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    @Singleton
    @Provides
    fun provideRemoteSource(): RemoteSource {
        val remote = Firebase.remoteConfig
        return RemoteConfigWrapper(remote)
    }
}
