package io.github.leoallvez.take.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import io.github.leoallvez.take.BuildConfig.*

@Entity(
    tableName = "media_items",
    foreignKeys = [
        ForeignKey(
            entity = Suggestion::class,
            parentColumns = ["suggestion_db_id"],
            childColumns =  ["suggestion_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
class MediaItem (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "media_db_id")
    var dbId: Long = 0,
    @field:Json(name = "id")
    @ColumnInfo(name = "api_id")
    val apiId: Long,
    val name: String?,
    val title: String?,
    @field:Json(name = "poster_path")
    @ColumnInfo(name = "poster_path")
    val posterPath: String,
    @field:Json(name = "backdrop_path")
    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String?,
    @field:Json(name = "vote_average")
    @ColumnInfo(name = "vote_average")
    val voteAverage: Double,
    @ColumnInfo(name = "suggestion_id", index = true)
    var suggestionId: Long = 0,
) {
    fun getItemTitle() = name ?: title ?: ""

    fun getItemPoster() = "$IMG_URL/$posterPath"

    fun getItemBackdrop() = "$IMG_URL/$backdropPath"
}
