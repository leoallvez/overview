package br.com.deepbyte.overview.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.deepbyte.overview.data.db.dao.MediaItemDao
import br.com.deepbyte.overview.data.db.dao.StreamingDao
import br.com.deepbyte.overview.data.db.dao.SuggestionDao
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.data.model.Suggestion
import br.com.deepbyte.overview.data.model.provider.Streaming

@Database(
    entities = [MediaItem::class, Suggestion::class, Streaming::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [ AutoMigration (from = 1, to = 2) ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mediaItemDao(): MediaItemDao
    abstract fun suggestionDao(): SuggestionDao
    abstract fun streamingDao(): StreamingDao
}
