package br.com.deepbyte.take.data.api.response

import br.com.deepbyte.take.BuildConfig.IMG_URL
import br.com.deepbyte.take.data.model.MediaItem
import br.com.deepbyte.take.util.DateHelper
import com.squareup.moshi.Json

data class MediaDetailResponse(
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

    var type: String? = null,

    val genres: List<Genre> = listOf(),
    private val credits: Credits = Credits(),
    val similar: Similar = Similar(),

    var providers: List<ProviderPlace> = listOf()
) : DataResponse() {

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
        "${runtime / 60}h ${runtime % 60}min"
    } else {
        ""
    }
}
data class Credits(val cast: List<PersonResponse> = listOf())

data class Genre(val name: String = "") : DataResponse()

data class Similar(val results: List<MediaItem> = listOf())
