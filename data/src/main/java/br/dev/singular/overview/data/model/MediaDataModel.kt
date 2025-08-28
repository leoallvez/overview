package br.dev.singular.overview.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.Date

@Serializable
@Entity(tableName = "media")
data class MediaDataModel(
    @PrimaryKey
    var id: Long = 0,
    val name: String = "",
    val title: String = "",
    @SerialName(value = "poster_path")
    @ColumnInfo(name = "poster_path")
    val posterPath: String = "",
    @SerialName(value = "media_type")
    val type: MediaDataType = MediaDataType.UNKNOWN,
    @Transient
    @ColumnInfo(name = "is_liked")
    var isLiked: Boolean = false,
    @Transient
    @ColumnInfo(name = "last_update")
    var lastUpdate: Date = Date()
) {
    @get:Ignore
    val betterTitle: String
        get() = name.ifEmpty { title }
}
