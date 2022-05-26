package io.github.leoallvez.take.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import io.github.leoallvez.take.BuildConfig

@Entity(
    tableName = "audio_visual_items",
    foreignKeys = [
        ForeignKey(
            entity = Suggestion::class,
            parentColumns = ["db_id"],
            childColumns =  ["suggestion_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
class AudioVisualItem (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "db_id")
    var dbId: Long = 0,
    @field:Json(name = "id")
    @ColumnInfo(name = "api_id")
    val apiId: Long,
    //val name: String?,
    //val title: String?,
    @field:Json(name = "poster_path")
    @ColumnInfo(name = "poster_path")
    val posterPath: String,
    @field:Json(name = "backdrop_path")
    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String,
    @field:Json(name = "vote_average")
    @ColumnInfo(name = "vote_average")
    val voteAverage: Double,
    @ColumnInfo(name = "suggestion_id", index = true)
    var suggestionId: Long = 0,
    var type: String = "OK"
) {
    fun getItemTitle() = EMPTY_TITLE

    fun getImageUrl() = "${BuildConfig.IMG_URL}$posterPath"

    companion object {
        const val EMPTY_TITLE = ""
        const val MOVIE_TYPE = "movie"
        const val TV_TYPE = "tv"
    }
}

