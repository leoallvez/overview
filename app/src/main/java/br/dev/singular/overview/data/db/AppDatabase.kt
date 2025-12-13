package br.dev.singular.overview.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.dev.singular.overview.data.db.dao.GenreDao
import br.dev.singular.overview.data.db.dao.MediaTypeDao
import br.dev.singular.overview.data.db.dao.StreamingDao
import br.dev.singular.overview.data.local.database.Converters
import br.dev.singular.overview.data.local.database.dao.SuggestionDao
import br.dev.singular.overview.data.local.database.dao.MediaDao
import br.dev.singular.overview.data.local.database.dao.StreamingDao as StreamingDaoNew
import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.StreamingDataModel
import br.dev.singular.overview.data.model.SuggestionDataModel
import br.dev.singular.overview.data.model.media.GenreEntity
import br.dev.singular.overview.data.model.media.MediaTypeEntity
import br.dev.singular.overview.data.model.media.MediaTypeGenresCrossRef
import br.dev.singular.overview.data.model.provider.StreamingEntity

@TypeConverters(Converters::class)
@Database(
    entities = [
        GenreEntity::class,
        StreamingEntity::class,
        MediaTypeEntity::class,
        MediaTypeGenresCrossRef::class,
        // data module Entity
        MediaDataModel::class,
        StreamingDataModel::class,
        SuggestionDataModel::class,
    ],
    version = 4,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun genreDao(): GenreDao
    abstract fun streamingDao(): StreamingDao
    abstract fun mediaTypeDao(): MediaTypeDao
    // data module Dao
    abstract fun mediaDao(): MediaDao

    abstract fun streamingDaoNew(): StreamingDaoNew

    abstract fun suggestionDao(): SuggestionDao
}
