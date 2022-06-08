package io.github.leoallvez.take.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.leoallvez.take.data.db.dao.MediaItemDao
import io.github.leoallvez.take.data.db.dao.SuggestionDao
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.model.Suggestion

@Database(
    entities = [MediaItem::class, Suggestion::class],
    version = 1,
    exportSchema = false
)
abstract class TakeDatabase : RoomDatabase() {
    abstract fun mediaItemDao(): MediaItemDao
    abstract fun suggestionDao(): SuggestionDao
}
