package io.github.leoallvez.take.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "tv_shows")
data class TvShow(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tv_show_id")
    var tvShowId: Long,
    @Json(name = "id")
    @ColumnInfo(name = "api_id")
    override val apiId: Long,
    val name: String,
    @ColumnInfo(name = "poster_path")
    override val posterPath: String,
    @ColumnInfo(name = "vote_average")
    override val voteAverage: Double
) : EntertainmentContent {
    override fun getTitleDescription(): String = name
}
