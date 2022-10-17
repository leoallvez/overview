package io.github.leoallvez.take.data.api.response

import com.squareup.moshi.Json
import io.github.leoallvez.take.BuildConfig.IMG_URL
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.util.DateHelper
import io.github.leoallvez.take.data.api.response.PersonResponse as Person

data class MediaDetailResponse (
    @field:Json(name = "id")
    val apiId: Long = 0,

    @field:Json(name = "backdrop_path")
    private val backdropPath: String = "",

    @field:Json(name = "original_title")
    private val originalTitle: String? = null,

    @field:Json(name = "original_name")
    private val originalName: String? = null,

    val overview: String = "",

    @field:Json(name = "poster_path")
    private val posterPath: String? = "",

    @field:Json(name = "release_date")
    private val releaseDate: String = "",

    private val runtime: Long = 0,

    private val title: String? = null,
    private val name: String? = null,

    val genres: List<Genre> = listOf(),
    private val credits: Credits = Credits(),
    val similar: Similar = Similar(),

    var providers: List<ProviderPlace> = listOf()
) {
    private val mediaTitle: String?
        get() = title ?: originalTitle

    private val mediaName: String
        get() = name ?: originalName ?: ""

    fun getLetter() = mediaTitle ?: mediaName

    fun getPosterImage() = "$IMG_URL/$posterPath"

    fun getBackdropImage() = "$IMG_URL/$backdropPath"

    fun getOrderedCast() = credits.cast.sortedBy { it.order }

    fun getReleaseYear() = DateHelper(releaseDate).getYear()

    fun getRuntimeFormatted() = if (runtime > 0) {
        "${runtime/60}h ${runtime%60}min"
    } else {
        ""
    }
}
data class Credits (val cast: List<Person> = listOf())

data class Genre (
    @field:Json(name = "id")
    val apiId: Long = 0,
    val name: String = ""
)

data class Similar (val results: List<MediaItem> = listOf())
