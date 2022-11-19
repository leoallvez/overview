package br.com.deepbyte.overview.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import br.com.deepbyte.overview.data.db.TakeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        TakeDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Provides
    fun provideMediaItemDao(db: TakeDatabase) = db.mediaItemDao()

    @Provides
    fun provideSuggestionDao(db: TakeDatabase) = db.suggestionDao()

    @Provides
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.dataStore

    companion object {
        private val Context.dataStore by preferencesDataStore("app_setting")
        private const val DATABASE_NAME = "take_database"
    }
}
