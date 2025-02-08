package br.dev.singular.overview.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@Entity(tableName = "suggestions")
data class SuggestionDataModel(
    @PrimaryKey(autoGenerate = true)
    @Transient
    var id: Long = 0,
    val path: String,
    val order: Int,
    val type: MediaDataType = MediaDataType.UNKNOWN,
    @ColumnInfo(name = "title_key")
    val titleKey: String,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean
)
