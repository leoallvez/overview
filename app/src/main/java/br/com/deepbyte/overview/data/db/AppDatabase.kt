package br.com.deepbyte.overview.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteTable
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import br.com.deepbyte.overview.data.db.dao.GenreDao
import br.com.deepbyte.overview.data.db.dao.MediaTypeDao
import br.com.deepbyte.overview.data.db.dao.StreamingDao
import br.com.deepbyte.overview.data.model.media.Genre
import br.com.deepbyte.overview.data.model.media.MediaSuggestion
import br.com.deepbyte.overview.data.model.media.MediaType
import br.com.deepbyte.overview.data.model.media.MediaTypeGenresCrossRef
import br.com.deepbyte.overview.data.model.provider.Streaming

@Database(
    entities = [
        Genre::class,
        Streaming::class,
        MediaType::class,
        MediaSuggestion::class,
        MediaTypeGenresCrossRef::class
    ],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = AppDatabase.DeleteTablesMigration::class),
        AutoMigration(from = 2, to = 3)
    ]
)
abstract class AppDatabase : RoomDatabase() {

    @DeleteTable(tableName = "media_items")
    @DeleteTable(tableName = "suggestions")
    class DeleteTablesMigration : AutoMigrationSpec

    abstract fun genreDao(): GenreDao
    abstract fun streamingDao(): StreamingDao
    abstract fun mediaTypeDao(): MediaTypeDao
}
