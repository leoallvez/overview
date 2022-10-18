package io.github.leoallvez.take.data.api.response

import com.squareup.moshi.Json
import io.github.leoallvez.take.BuildConfig
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.util.DateHelper

data class PersonResponse (
    @field:Json(name = "id")
    val apiId: Long = 0,
    val name: String,
    val biography: String,
    val birthday: String,
    val order: Int = 0,
    val character: String = "",
    @field:Json(name = "deathday")
    val deathDay: String? = null,
    @field:Json(name = "place_of_birth")
    private val placeOfBirth: String?,
    @field:Json(name = "profile_path")
    private val profilePath: String,
    @field:Json(name = "tv_credits")
    private val tvShows: MediaCredits,
    @field:Json(name = "movie_credits")
    private val movies: MediaCredits
) {
    fun getProfileImage() = "${BuildConfig.IMG_URL}/$profilePath"
    fun getFilmography() = movies.mediaItems
    fun getTvShows() = tvShows.mediaItems
    fun getFormattedBirthday() = DateHelper(birthday).formattedDate()
    fun getFormattedDeathDay() = DateHelper(deathDay).formattedDate()
    fun birthPlace() = placeOfBirth ?: ""
    fun getAge() = DateHelper(birthday).periodBetween(deathDay)
}

data class MediaCredits (
    @field:Json(name = "cast")
    val mediaItems: List<MediaItem>
)

