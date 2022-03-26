package io.github.leoallvez.take.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.leoallvez.take.db.MovieDao
import io.github.leoallvez.take.db.TakeDatabase
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
            "take_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideTaskDao(db: TakeDatabase): MovieDao {
        return db.movieDao()
    }
}

