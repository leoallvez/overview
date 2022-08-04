package io.github.leoallvez.take.data.api.response

import com.squareup.moshi.Json
import io.github.leoallvez.take.BuildConfig
import io.github.leoallvez.take.BuildConfig.*

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
    val originalTitle: String = "",

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
    val title: String = "",
    val video: Boolean = false,

    @field:Json(name = "vote_average")
    val voteAverage: Double = 0.0,

    @field:Json(name = "vote_count")
    val voteCount: Long = 0L
) {
    fun getItemBackdrop() = "$IMG_URL/$backdropPath"
}

data class Genre (
    val id: Long,
    val name: String
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
