package br.dev.singular.overview.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.dev.singular.overview.data.db.dao.GenreDao
import br.dev.singular.overview.data.db.dao.MediaDao as MediaDaoOld
import br.dev.singular.overview.data.db.dao.MediaTypeDao
import br.dev.singular.overview.data.db.dao.StreamingDao
import br.dev.singular.overview.data.local.database.Converters
import br.dev.singular.overview.data.local.database.dao.SuggestionDao
import br.dev.singular.overview.data.local.database.dao.MediaDao
import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.SuggestionDataModel
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
        MediaTypeGenresCrossRef::class,
        // data module Entity
        MediaDataModel::class,
        SuggestionDataModel::class
    ],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun genreDao(): GenreDao
    abstract fun mediaDaoOld(): MediaDaoOld
    abstract fun streamingDao(): StreamingDao
    abstract fun mediaTypeDao(): MediaTypeDao
    // data module Dao
    abstract fun mediaDao(): MediaDao
    abstract fun suggestionDao(): SuggestionDao
}
