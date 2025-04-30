package br.dev.singular.overview.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "suggestions")
class SuggestionDataModel(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val path: String,
    val order: Int,
    val type: String,
    val titleKey: String,
    val isActive: Boolean
)
