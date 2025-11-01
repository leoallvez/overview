package br.dev.singular.overview.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MediaDataType(val key: String) {
    @SerialName("movie")
    MOVIE("movie"),
    @SerialName("tv")
    TV("tv"),
    @SerialName("all")
    ALL("all"),
    @SerialName("unknown")
    UNKNOWN("unknown");

    companion object {
        fun fromKey(key: String) = entries.find { it.key == key } ?: UNKNOWN
    }
}
