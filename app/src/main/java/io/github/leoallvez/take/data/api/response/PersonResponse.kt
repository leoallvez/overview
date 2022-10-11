package io.github.leoallvez.take.data.api.response

import com.squareup.moshi.Json
import io.github.leoallvez.take.BuildConfig
import io.github.leoallvez.take.data.model.MediaItem

data class PersonResponse (
    val id: Long,
    val name: String,
    val biography: String,
    val birthday: String,
    @field:Json(name = "deathday")
    val deathDay: String? = null,
    @field:Json(name = "known_for_department")
    val knownForDepartment: String,
    @field:Json(name = "place_of_birth")
    val placeOfBirth: String,
    @field:Json(name = "profile_path")
    val profilePath: String,
    @field:Json(name = "tv_credits")
    val tvCredits: TvCredits,
    @field:Json(name = "movie_credits")
    val movieCredits: MovieCredits
) {
    override fun toString() = """name: $name \n biography: $biography\n crew size ${movieCredits.crew.size} \n
             movie cast size ${movieCredits.crew.size}, tv cast size ${tvCredits.cast.size}"""

    fun getProfile() = "${BuildConfig.IMG_URL}/$profilePath"
}

data class MovieCredits (
    val cast: List<MediaItem>,
    val crew: List<MediaItem>
)

data class TvCredits (
    val cast: List<MediaItem>,
    val crew: List<MediaItem>
)
