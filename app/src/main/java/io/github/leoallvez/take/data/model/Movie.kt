package io.github.leoallvez.take.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(
    tableName = "movies",
    foreignKeys = [
        ForeignKey(
            entity = Suggestion::class,
            parentColumns = ["suggestion_id"],
            childColumns =  ["suggestion_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class Movie(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "movie_id")
    var movieId: Long,
    @field:Json(name = "id")
    @ColumnInfo(name = "api_id")
    override val apiId: Long,
    val title: String?,
    @field:Json(name = "poster_path")
    @ColumnInfo(name = "poster_path")
    override val posterPath: String?,
    @field:Json(name = "vote_average")
    @ColumnInfo(name = "vote_average")
    override val voteAverage: Double,
    @ColumnInfo(name = "suggestion_id", index = true)
    override var suggestionId: Long = 0,
) : AudioVisual {
    override fun getContentTitle(): String = title ?: "Title is null"
}
