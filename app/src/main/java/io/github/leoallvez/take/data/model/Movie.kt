package io.github.leoallvez.take.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "movie_id")
    var movieId: Long,
    @field:Json(name = "id")
    @ColumnInfo(name = "api_id")
    override val apiId: Long,
    val title: String,
    @field:Json(name = "poster_path")
    @ColumnInfo(name = "poster_path")
    override val posterPath: String,
    @field:Json(name = "vote_average")
    @ColumnInfo(name = "vote_average")
    override val voteAverage: Double
): Audiovisual {
    override fun getTitleDescription(): String = title
}