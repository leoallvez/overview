package br.dev.singular.overview.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "media_type_genre",
    primaryKeys = ["type", "genre_id"]
)
data class MediaTypeGenreDataModel(
    @ColumnInfo(name = "type", index = true)
    val type: MediaDataType,
    @ColumnInfo(name = "genre_id", index = true)
    val genreId: Long
)
