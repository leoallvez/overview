package br.dev.singular.overview.data.model.person

import br.dev.singular.overview.BuildConfig
import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.model.media.Movie
import br.dev.singular.overview.data.model.media.TvShow
import com.squareup.moshi.Json
import br.dev.singular.overview.util.DateHelper

data class Person(
    @field:Json(name = "id")
    val apiId: Long = 0,
    val name: String = "",
    val biography: String = "",
    val birthday: String = "",
    val order: Int = 0,
    val job: String = "",
    private val character: String = "",
    @field:Json(name = "deathday")
    val deathDay: String? = null,
    @field:Json(name = "place_of_birth")
    private val placeOfBirth: String? = "",
    @field:Json(name = "profile_path")
    private val profilePath: String = "",
    @field:Json(name = "tv_credits")
    private val tvShows: MediaCredits<TvShow> = MediaCredits(),
    @field:Json(name = "movie_credits")
    private val movies: MediaCredits<Movie> = MediaCredits()
) {
    fun getProfileImage() = "${BuildConfig.TMDB_IMG_URL}/$profilePath"
    fun getFilmography() = movies.items
    fun getTvShows() = tvShows.items
    fun getFormattedBirthday() = DateHelper(birthday).formattedDate()
    fun getFormattedDeathDay() = DateHelper(deathDay).formattedDate()
    fun birthPlace() = placeOfBirth ?: ""
    fun getAge() = DateHelper(birthday).periodBetween(deathDay)
    fun getCharacterName(): String {
        val list = character.split("/")
        return if (list.isNotEmpty()) list.first() else ""
    }
}

class MediaCredits<T : Media>(
    @field:Json(name = "cast")
    val items: List<T> = listOf()
)
