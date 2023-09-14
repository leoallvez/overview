package br.com.deepbyte.overview.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.deepbyte.overview.data.db.dao.GenreDao
import br.com.deepbyte.overview.data.db.dao.MediaDao
import br.com.deepbyte.overview.data.db.dao.MediaTypeDao
import br.com.deepbyte.overview.data.db.dao.StreamingDao
import br.com.deepbyte.overview.data.model.media.GenreEntity
import br.com.deepbyte.overview.data.model.media.MediaEntity
import br.com.deepbyte.overview.data.model.media.MediaTypeEntity
import br.com.deepbyte.overview.data.model.media.MediaTypeGenresCrossRef
import br.com.deepbyte.overview.data.model.provider.StreamingEntity

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
