package io.github.leoallvez.take.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.leoallvez.take.data.db.TakeDatabase
import io.github.leoallvez.take.data.db.dao.MovieDao
import io.github.leoallvez.take.data.db.dao.SuggestionsDao
import io.github.leoallvez.take.data.db.dao.TvShowDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): TakeDatabase {
        return Room.databaseBuilder(
            context,
            TakeDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideMovieDao(db: TakeDatabase): MovieDao {
        return db.movieDao()
    }

    @Provides
    fun provideTvShowDao(db: TakeDatabase): TvShowDao {
        return db.tvShowDao()
    }

    @Provides
    fun provideSuggestions(db: TakeDatabase): SuggestionsDao {
        return db.suggestionDao()
    }

    @Provides
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.dataStore
    }

    companion object {
        private val Context.dataStore by preferencesDataStore("app_setting")
        private const val DATABASE_NAME = "take_database"
    }
}
