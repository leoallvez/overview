package io.github.leoallvez.take.data.api.response

import com.squareup.moshi.Json
import io.github.leoallvez.take.BuildConfig.IMG_URL

data class MediaDetailResponse (
    val adult: Boolean = false,

    @field:Json(name = "backdrop_path")
    val backdropPath: String = "",

    @field:Json(name = "belongs_to_collection")
    val belongsToCollection: Any? = null,

    val budget: Long = 0L,
    val genres: List<Genre> = listOf(),
    val homepage: String = "",
    val id: Long = 0L,

    @field:Json(name = "imdb_id")
    val imdbID: String = "",

    @field:Json(name = "original_language")
    val originalLanguage: String = "",

    @field:Json(name = "original_title")
    val originalTitle: String? = null,

    @field:Json(name = "original_name")
    val originalName: String? = null,

    val overview: String = "",
    val popularity: Double = 0.0,

    @field:Json(name = "poster_path")
    val posterPath: String? = "",

    @field:Json(name = "production_companies")
    val productionCompanies: List<ProductionCompany> = listOf(),

    @field:Json(name = "production_countries")
    val productionCountries: List<ProductionCountry> = listOf(),

    @field:Json(name = "release_date")
    val releaseDate: String = "",

    val revenue: Long = 0L,
    val runtime: Long = 0L,

    @field:Json(name = "spoken_languages")
    val spokenLanguages: List<SpokenLanguage> = listOf(),

    val status: String = "",
    val tagline: String = "",
    val title: String? = null,
    val name: String? = null,
    val video: Boolean = false,

    @field:Json(name = "vote_average")
    val voteAverage: Double = 0.0,

    @field:Json(name = "vote_count")
    val voteCount: Long = 0L,
    private val credits: Credits = Credits()
) {
    private val mediaTitle: String?
        get() = title ?: originalTitle

    private val mediaName: String
        get() = name ?: originalName ?: ""

    fun getMediaDetailsLetter() = mediaTitle ?: mediaName

    fun getPoster() = "$IMG_URL/$posterPath"

    fun getBackdrop() = "$IMG_URL/$backdropPath"

    fun getOrderedCast(): List<Person> = credits.cast.sortedBy { it.order }

    fun releaseYear() = releaseDate.split("-").first()

    fun getRuntimeFormatted() = if (runtime > 0) {
        val hours = runtime / 60
        val minutes = runtime % 60
        "${hours}h ${minutes}m"
    } else {
        ""
    }

}

data class Genre (
    val id: Long = 0,
    val name: String = ""
)

data class ProductionCompany (
    val id: Long,

    @field:Json(name = "logo_path")
    val logoPath: String? = null,

    val name: String,

    @field:Json(name = "origin_country")
    val originCountry: String
)

data class ProductionCountry (
    @field:Json(name = "iso_3166_1")
    val iso3166_1: String,

    val name: String
)

data class SpokenLanguage (
    @field:Json(name = "english_name")
    val englishName: String,

    @field:Json(name = "iso_639_1")
    val iso639_1: String,

    val name: String
)

data class Credits (
    val cast: List<Person> = listOf(),
    val crew: List<Person> = listOf(),
)

data class Person(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    @field:Json(name = "known_for_department")
    val knownForDepartment: String,
    val name: String,
    @field:Json(name = "original_name")
    val originalName: String,
    val popularity: Double = 0.0,
    @field:Json(name = "profile_path")
    val profilePath: String,
    val character: String,
    @field:Json(name = "credit_id")
    val creditId: String,
    val order: Int,
) {
    fun getProfile() = "$IMG_URL/$profilePath"
}
