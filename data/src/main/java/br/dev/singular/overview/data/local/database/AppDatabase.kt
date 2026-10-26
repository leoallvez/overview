package br.dev.singular.overview.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.dev.singular.overview.data.local.database.dao.CatalogDao
import br.dev.singular.overview.data.local.database.dao.GenreDao
import br.dev.singular.overview.data.local.database.dao.PendingFavoriteDao
import br.dev.singular.overview.data.local.database.dao.PredictionDao
import br.dev.singular.overview.data.local.database.dao.SuggestionDao
import br.dev.singular.overview.data.model.CatalogDataModel
import br.dev.singular.overview.data.model.GenreDataModel
import br.dev.singular.overview.data.model.MediaTypeGenreDataModel
import br.dev.singular.overview.data.model.PendingFavoriteDataModel
import br.dev.singular.overview.data.model.PredictionDataModel
import br.dev.singular.overview.data.model.SuggestionDataModel

@TypeConverters(Converters::class)
@Database(
    entities = [
        GenreDataModel::class,
        CatalogDataModel::class,
        SuggestionDataModel::class,
        MediaTypeGenreDataModel::class,
        PendingFavoriteDataModel::class,
        PredictionDataModel::class,
    ],
    version = 5,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun genreDao(): GenreDao

    abstract fun catalogDao(): CatalogDao

    abstract fun suggestionDao(): SuggestionDao

    abstract fun pendingFavoriteDao(): PendingFavoriteDao

    abstract fun predictionDao(): PredictionDao
}
