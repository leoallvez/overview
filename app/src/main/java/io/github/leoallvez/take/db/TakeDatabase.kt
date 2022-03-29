package io.github.leoallvez.take.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.leoallvez.take.data.model.Movie

@Database(
    entities = [Movie::class],
    version = 1,
    exportSchema = false
)
abstract class TakeDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}