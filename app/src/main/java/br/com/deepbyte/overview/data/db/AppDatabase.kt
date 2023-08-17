package br.com.deepbyte.overview.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.deepbyte.overview.data.db.dao.GenreDao
import br.com.deepbyte.overview.data.db.dao.MediaDao
import br.com.deepbyte.overview.data.db.dao.MediaTypeDao
import br.com.deepbyte.overview.data.db.dao.StreamingDao
import br.com.deepbyte.overview.data.model.media.Genre
import br.com.deepbyte.overview.data.model.media.MediaEntity
import br.com.deepbyte.overview.data.model.media.MediaType
import br.com.deepbyte.overview.data.model.media.MediaTypeGenresCrossRef
import br.com.deepbyte.overview.data.model.provider.Streaming

@Database(
    entities = [
        Genre::class,
        Streaming::class,
        MediaType::class,
        MediaEntity::class,
        MediaTypeGenresCrossRef::class
    ],
    version = 1,
    exportSchema = true
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun genreDao(): GenreDao
    abstract fun streamingDao(): StreamingDao
    abstract fun mediaTypeDao(): MediaTypeDao
    abstract fun mediaDao(): MediaDao
}
