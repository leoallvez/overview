package io.github.leoallvez.take.data.model

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
}

class MovieSuggestion(
    @Embedded
    val suggestion: Suggestion,
    @Relation(
        parentColumn = "suggestion_id",
        entityColumn = "suggestion_id"
    )
    val movies: List<Movie>
) {
    fun toSuggestionResult(): SuggestionResult {
        return SuggestionResult(
            titleResourceId = suggestion.titleResourceId,
            audiovisuals = movies
        )
    }
}

class TvShowSuggestion(
    @Embedded
    val suggestion: Suggestion,
    @Relation(
        parentColumn = "suggestionId",
        entityColumn = "suggestionId"
    )
    val tvShows: List<TvShow>
) {
    fun toSuggestionResult(): SuggestionResult {
        return SuggestionResult(
            titleResourceId = suggestion.titleResourceId,
            audiovisuals = tvShows
        )
    }
}

class SuggestionResult(
    val titleResourceId: String,
    val audiovisuals: List<Audiovisual>
)
