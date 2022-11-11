package br.com.deepbyte.take.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.deepbyte.take.data.db.dao.MediaItemDao
import br.com.deepbyte.take.data.db.dao.SuggestionDao
import br.com.deepbyte.take.data.model.MediaItem
import br.com.deepbyte.take.data.model.Suggestion

@Database(
    entities = [MediaItem::class, Suggestion::class],
    version = 1,
    exportSchema = false
)
abstract class TakeDatabase : RoomDatabase() {
    abstract fun mediaItemDao(): MediaItemDao
    abstract fun suggestionDao(): SuggestionDao
}
