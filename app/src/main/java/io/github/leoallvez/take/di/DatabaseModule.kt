package io.github.leoallvez.take.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.leoallvez.take.data.db.TakeDatabase
import io.github.leoallvez.take.data.db.dao.MovieDao
import io.github.leoallvez.take.data.db.dao.TvShowDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

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

    companion object {
        private const val DATABASE_NAME = "take_database"
    }
}
