package br.dev.singular.overview.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import br.dev.singular.overview.data.db.AppDatabase
import br.dev.singular.overview.data.db.Migration
import br.dev.singular.overview.data.db.SEEDS_CALLBACK
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
        AppDatabase::class.java,
        DATABASE_NAME
    )
        .addCallback(SEEDS_CALLBACK)
        .addMigrations(
            Migration(from = 1, to = 2, context),
            Migration(from = 2, to = 3, context),
            Migration(from = 3, to = 4, context),
            Migration(from = 4, to = 5, context)
        ).build()

    @Provides
    fun provideCatalogDao(db: AppDatabase) = db.catalogDao()

    @Provides
    fun provideGenreDao(db: AppDatabase) = db.genreDao()

    @Provides
    fun provideMediaDao(db: AppDatabase) = db.mediaDao()

    @Provides
    fun provideSuggestionDao(db: AppDatabase) = db.suggestionDao()

    @Provides
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.dataStore

    companion object {
        private val Context.dataStore by preferencesDataStore("app_setting")
        private const val DATABASE_NAME = "database_overview"
    }
}
