package io.github.leoallvez.take.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Suggestions (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "suggestions_id")
    @Transient
    var suggestionsId: Long = 0,

    val order: Int,
    @SerializedName(value = "api_path")
    val apiPath: String,
    @SerializedName(value = "title_resource_id")
    val titleResourceId: String,
)
