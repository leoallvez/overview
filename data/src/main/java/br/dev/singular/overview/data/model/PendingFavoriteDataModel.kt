package br.dev.singular.overview.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.Date

@Entity(tableName = "pending_favorite", primaryKeys = ["type", "id"])
data class PendingFavoriteDataModel(
    val id: Long = 0,
    val type: MediaDataType = MediaDataType.UNKNOWN,
    val title: String = "",
    @ColumnInfo(name = "poster_path")
    val posterPath: String = "",
    @ColumnInfo(name = "last_update")
    val lastUpdate: Date = Date()
)
