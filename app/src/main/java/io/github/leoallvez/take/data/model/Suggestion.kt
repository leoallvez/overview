package io.github.leoallvez.take.data.model

import androidx.compose.runtime.Immutable
import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "suggestions")
data class Suggestion (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "suggestion_id")
    var suggestionId: Long = 0,
    val type: String,
    val order: Int,
    @ColumnInfo(name = "api_path")
    @SerializedName(value = "api_path")
    val apiPath: String,
    @ColumnInfo(name = "title_resource_id")
    @SerializedName(value = "title_resource_id")
    val titleResourceId: String,
) {
    companion object {
        const val MOVIE_TYPE   = "movie"
        const val TV_SHOW_TYPE = "tv_show"
    }

    fun toSuggestionResult(
        audiovisuals: List<Audiovisual>
    ): SuggestionResult {
        return SuggestionResult(
            order = order,
            titleResourceId = titleResourceId,
            audiovisuals = audiovisuals
        )
    }
}

@Immutable
class SuggestionResult(
    val order: Int,
    val titleResourceId: String,
    val audiovisuals: List<Audiovisual>
)
