package br.dev.singular.overview.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MediaDataType(val value: String) {
    @SerialName("movie")
    MOVIE("movie"),
    @SerialName("tv")
    TV("tv"),
    @SerialName("all")
    ALL("all"),
    @SerialName("unknown")
    UNKNOWN("unknown");

    companion object {
        fun fromValue(value: String): MediaDataType {
            return entries.find { it.value == value } ?: UNKNOWN
        }
    }
}
