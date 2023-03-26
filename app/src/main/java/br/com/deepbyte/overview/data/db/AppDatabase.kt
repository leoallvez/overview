package br.com.deepbyte.overview.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.deepbyte.overview.data.db.dao.*
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.data.model.Suggestion
import br.com.deepbyte.overview.data.model.media.Genre
import br.com.deepbyte.overview.data.model.media.GenreType
import br.com.deepbyte.overview.data.model.provider.Streaming

@Database(
    entities = [
        Genre::class,
        Streaming::class,
        GenreType::class,
        MediaItem::class,
        Suggestion::class
    ],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mediaItemDao(): MediaItemDao
    abstract fun suggestionDao(): SuggestionDao
    abstract fun streamingDao(): StreamingDao
    abstract fun genreTypeDao(): GenreTypeDao
    abstract fun genreDao(): GenreDao
}
