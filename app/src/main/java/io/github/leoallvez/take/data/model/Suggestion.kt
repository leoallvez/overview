package io.github.leoallvez.take.data.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "suggestions")
data class Suggestion(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "db_id")
    var dbId: Long = 0,
    val type: String,
    val order: Int,
    @ColumnInfo(name = "api_path")
    @SerializedName(value = "api_path")
    val apiPath: String,
    @ColumnInfo(name = "title_resource_id")
    @SerializedName(value = "title_resource_id")
    val titleResourceId: String,
) {

    fun toMediaSuggestion(items: List<MediaItem>): MediaSuggestion {
        return MediaSuggestion(order, titleResourceId, items)
    }
}
