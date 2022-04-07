package io.github.leoallvez.take.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "tv_shows")
class TvShow (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tv_show_id")
    @Transient
    var tvShowId: Long = 0,

    val id: Long,
    val name: String,

    @Json(name = "poster_path")
    val posterPath: String,

    @Json(name = "vote_average")
    val voteAverage: Double,
)
