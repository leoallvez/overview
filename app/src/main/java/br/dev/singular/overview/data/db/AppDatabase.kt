package br.dev.singular.overview.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.dev.singular.overview.data.db.dao.GenreDao
import br.dev.singular.overview.data.db.dao.MediaDao
import br.dev.singular.overview.data.db.dao.MediaTypeDao
import br.dev.singular.overview.data.db.dao.StreamingDao
import br.dev.singular.overview.data.model.media.GenreEntity
import br.dev.singular.overview.data.model.media.MediaEntity
import br.dev.singular.overview.data.model.media.MediaTypeEntity
import br.dev.singular.overview.data.model.media.MediaTypeGenresCrossRef
import br.dev.singular.overview.data.model.provider.StreamingEntity

@TypeConverters(Converters::class)
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
