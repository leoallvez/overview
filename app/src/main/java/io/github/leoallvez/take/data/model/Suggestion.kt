package io.github.leoallvez.take.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "suggestions")
data class Suggestion (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "suggestions_id")
    var suggestionsId: Long = 0,

    val order: Int,
    @ColumnInfo(name = "api_path")
    @SerializedName(value = "api_path")
    val apiPath: String,
    @ColumnInfo(name = "title_resource_id")
    @SerializedName(value = "title_resource_id")
    val titleResourceId: String,
)
