package dev.com.singular.overview.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.com.singular.overview.data.model.provider.StreamingEntity
import dev.com.singular.overview.data.db.dao.GenreDao
import dev.com.singular.overview.data.db.dao.MediaDao
import dev.com.singular.overview.data.db.dao.MediaTypeDao
import dev.com.singular.overview.data.db.dao.StreamingDao
import dev.com.singular.overview.data.model.media.GenreEntity
import dev.com.singular.overview.data.model.media.MediaEntity
import dev.com.singular.overview.data.model.media.MediaTypeEntity
import dev.com.singular.overview.data.model.media.MediaTypeGenresCrossRef

@Database(
    entities = [
        GenreEntity::class,
        MediaEntity::class,
        StreamingEntity::class,
        MediaTypeEntity::class,
        MediaTypeGenresCrossRef::class
    ],
    version = 1,
    exportSchema = true
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun genreDao(): GenreDao
    abstract fun mediaDao(): MediaDao
    abstract fun streamingDao(): StreamingDao
    abstract fun mediaTypeDao(): MediaTypeDao
}
