package br.dev.singular.overview.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonDataModel(
    val id: Long = 0,
    val order: Int = 0,
    val job: String = "",
    val name: String = "",
    val birthday: String = "",
    @SerialName(value = "deathday")
    val deathDay: String = "",
    val biography: String = "",
    val character: String = "",
    val profilePath: String = "",
    val placeOfBirth: String = "",
    @SerialName(value = "tv_credits")
    internal val tvShowsCredits: MediaCredits = MediaCredits(),
    @SerialName(value = "movie_credits")
    internal val moviesCredits:  MediaCredits = MediaCredits()
)

@Serializable
class MediaCredits(
    @SerialName(value = "cast")
    internal val list: List<MediaDataModel> = listOf()
)
