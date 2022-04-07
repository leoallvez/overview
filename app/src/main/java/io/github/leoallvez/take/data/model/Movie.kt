package io.github.leoallvez.take.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "movies")
data class Movie(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "movie_id")
    @Transient
    var movieId: Long = 0,

    val id: Long,
    val title: String,

    @Json(name = "poster_path")
    val posterPath: String,

    @Json(name = "vote_average")
    val voteAverage: Double,
)
