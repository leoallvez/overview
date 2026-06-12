package br.dev.singular.overview.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.dev.singular.overview.data.local.database.Converters
import br.dev.singular.overview.data.local.database.dao.CatalogDao
import br.dev.singular.overview.data.local.database.dao.GenreDao
import br.dev.singular.overview.data.local.database.dao.MediaDao
import br.dev.singular.overview.data.local.database.dao.SuggestionDao
import br.dev.singular.overview.data.model.CatalogDataModel
import br.dev.singular.overview.data.model.GenreDataModel
import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.MediaTypeGenreDataModel
import br.dev.singular.overview.data.model.SuggestionDataModel

@TypeConverters(Converters::class)
@Database(
    entities = [
        GenreDataModel::class,
        MediaDataModel::class,
        CatalogDataModel::class,
        SuggestionDataModel::class,
        MediaTypeGenreDataModel::class
    ],
    version = 5,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun genreDao(): GenreDao

    abstract fun mediaDao(): MediaDao

    abstract fun catalogDao(): CatalogDao

    abstract fun suggestionDao(): SuggestionDao
}
