package br.com.deepbyte.overview.data.model.media

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "media_type_cross_ref",
    primaryKeys = ["media_type_db_id", "genre_db_id"]
)
data class MediaTypeGenresCrossRef(
    @ColumnInfo(name = "media_type_db_id", index = true)
    val mediaTypeId: Long,
    @ColumnInfo(name = "genre_db_id", index = true)
    val genreId: Long
)
