package br.com.deepbyte.overview.data.model.media

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "genre_type_cross_ref",
    primaryKeys = ["genre_type_db_id", "genre_db_id"]
)
data class GenreTypeCrossRef(
    @ColumnInfo(name = "genre_type_db_id")
    val genreTypeId: Long,
    @ColumnInfo(name = "genre_db_id")
    val genreId: Long
)
