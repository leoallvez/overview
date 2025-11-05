package br.dev.singular.overview.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.Date

@Serializable
@Entity(tableName = "suggestion")
data class SuggestionDataModel(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val order: Int,
    val type: MediaDataType = MediaDataType.UNKNOWN,
    @ColumnInfo(name = "source_key")
    val sourceKey: String,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean,
    @Transient
    @ColumnInfo(name = "last_update")
    var lastUpdate: Date = Date()
)
