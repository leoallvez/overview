package br.com.deepbyte.overview.data.model

import br.com.deepbyte.overview.BuildConfig.IMG_URL
import com.squareup.moshi.Json

// TODO: Replace this class with Movie and TvShow;
class MediaItem(
    @field:Json(name = "id")
    val apiId: Long = 0,
    val name: String? = "",
    private val title: String? = "",
    @field:Json(name = "poster_path")
    val posterPath: String? = "",
    @field:Json(name = "vote_average")
    val voteAverage: Double = 0.0,
    @field:Json(name = "media_type")
    var type: String? = ""
) {
    fun getLetter() = if (!name.isNullOrEmpty()) name else title ?: ""

    fun getPosterImage() = "$IMG_URL/$posterPath"
}
