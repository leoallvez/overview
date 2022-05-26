package io.github.leoallvez.take.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.leoallvez.take.data.db.dao.AudioVisualItemDao
import io.github.leoallvez.take.data.db.dao.SuggestionsDao
import io.github.leoallvez.take.data.model.AudioVisualItem
import io.github.leoallvez.take.data.model.Suggestion

@Database(
    entities = [AudioVisualItem::class, Suggestion::class],
    version = 1,
    exportSchema = false
)
abstract class TakeDatabase : RoomDatabase() {
    abstract fun audioVisualItemDao(): AudioVisualItemDao
    abstract fun suggestionDao(): SuggestionsDao
}
