package io.github.leoallvez.take.data.api.response

import com.squareup.moshi.Json
import io.github.leoallvez.take.BuildConfig
import io.github.leoallvez.take.data.model.MediaItem

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
    val placeOfBirth: String,
    @field:Json(name = "profile_path")
    private val profilePath: String,
    @field:Json(name = "tv_credits")
    private val tvCredits: MediaCredits,
    @field:Json(name = "movie_credits")
    private val movieCredits: MediaCredits
) {
    fun getProfileImage() = "${BuildConfig.IMG_URL}/$profilePath"
    fun getFilmography() = movieCredits.cast
    fun getTvShows() = tvCredits.cast
}

data class MediaCredits (val cast: List<MediaItem>)
