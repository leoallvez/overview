package io.github.leoallvez.take.data.api.response

import com.squareup.moshi.Json
import io.github.leoallvez.take.BuildConfig.IMG_URL
import io.github.leoallvez.take.data.model.MediaItem

data class MediaDetailResponse (
    val id: Long = 0L,

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

    private val runtime: Long = 0L,

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

    fun getPoster() = "$IMG_URL/$posterPath"

    fun getBackdrop() = "$IMG_URL/$backdropPath"

    fun getOrderedCast(): List<Person> = credits.cast.sortedBy { it.order }

    fun getReleaseYear() = releaseDate.split("-").first()

    fun getRuntimeFormatted() = if (runtime > 0) {
        "${runtime/60}h ${runtime%60}min"
    } else {
        ""
    }
}

data class Similar (
    val results: List<MediaItem> = listOf(),
)

data class Genre (
    val id: Long = 0,
    val name: String = ""
)

data class Credits (
    val cast: List<Person> = listOf(),
    val crew: List<Person> = listOf(),
)

data class Person(
    val id: Long,
    val name: String,
    @field:Json(name = "profile_path")
    val profilePath: String? = "",
    val character: String,
    val order: Int,
) {
    fun getProfile() = "$IMG_URL/$profilePath"
}
