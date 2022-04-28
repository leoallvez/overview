package io.github.leoallvez.take.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.leoallvez.take.data.db.dao.MovieDao
import io.github.leoallvez.take.data.db.dao.SuggestionsDao
import io.github.leoallvez.take.data.db.dao.TvShowDao
import io.github.leoallvez.take.data.model.Movie
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.data.model.TvShow

@Database(
    entities = [Movie::class, TvShow::class, Suggestion::class],
    version = 1,
    exportSchema = false
)
abstract class TakeDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TvShowDao
    abstract fun suggestionDao(): SuggestionsDao
}
