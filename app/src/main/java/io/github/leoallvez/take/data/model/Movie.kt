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
    @Json(name = "id")
    @ColumnInfo(name = "api_id")
    override val apiId: Long,
    val title: String,
    @ColumnInfo(name = "poster_path")
    override val posterPath: String,
    @ColumnInfo(name = "vote_average")
    override val voteAverage: Double
): EntertainmentContent {
    override fun getTitleDescription(): String = title
}