package br.com.deepbyte.overview.data.api.response

import br.com.deepbyte.overview.BuildConfig
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.util.DateHelper
import com.squareup.moshi.Json

data class PersonResponse(
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
    private val tvShows: MediaCredits = MediaCredits(),
    @field:Json(name = "movie_credits")
    private val movies: MediaCredits = MediaCredits()
) : DataResponse() {
    fun getProfileImage() = "${BuildConfig.IMG_URL}/$profilePath"
    fun getFilmography() = movies.mediaItems
    fun getTvShows() = tvShows.mediaItems
    fun getFormattedBirthday() = DateHelper(birthday).formattedDate()
    fun getFormattedDeathDay() = DateHelper(deathDay).formattedDate()
    fun birthPlace() = placeOfBirth ?: ""
    fun getAge() = DateHelper(birthday).periodBetween(deathDay)
    fun getCharacterName(): String {
        val list = character.split("/")
        return if (list.isNotEmpty()) list.first() else ""
    }
}

data class MediaCredits(
    @field:Json(name = "cast")
    val mediaItems: List<MediaItem> = listOf()
)
