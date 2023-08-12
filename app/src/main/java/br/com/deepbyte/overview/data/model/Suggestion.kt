package br.com.deepbyte.overview.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

// TODO: Delete when delete MediaItem.kt
@Entity(tableName = "suggestions")
data class Suggestion(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "suggestion_db_id")
    var dbId: Long = 0,
    val type: String,
    val order: Int,
    @ColumnInfo(name = "api_path")
    @field:Json(name = "api_path")
    val apiPath: String,
    @ColumnInfo(name = "title_resource_id")
    @field:Json(name = "title_resource_id")
    val titleResourceId: String
)
