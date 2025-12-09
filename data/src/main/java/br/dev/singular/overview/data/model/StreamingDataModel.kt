package br.dev.singular.overview.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.Date

@Serializable
@Entity(tableName = "streaming")
data class StreamingDataModel(
    @PrimaryKey
    @SerialName(value = "provider_id")
    var id: Long = 0,
    @SerialName(value = "provider_name")
    val name: String = "",
    @SerialName(value = "display_priority")
    val priority: Int = 0,
    @SerialName(value = "logo_path")
    @ColumnInfo(name = "logo_path")
    val logoPath: String = "",
    var display: Boolean = true,
    @Transient
    @ColumnInfo(name = "last_update")
    var lastUpdate: Date = Date()
)
