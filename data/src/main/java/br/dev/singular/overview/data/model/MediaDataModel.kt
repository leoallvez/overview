package br.dev.singular.overview.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.Date

@Serializable
data class MediaDataModel(
    var id: Long = 0,
    val name: String = "",
    val title: String = "",
    @SerialName(value = "poster_path")
    val posterPath: String = "",
    @SerialName(value = "media_type")
    val type: MediaDataType = MediaDataType.UNKNOWN,
    @Transient
    var isLiked: Boolean = false,
    @Transient
    var lastUpdate: Date = Date(),
    @Transient
    val originalTitle: String = "",
    @Transient
    val genres: List<String> = emptyList()
) {
    val betterTitle: String
        get() = name.ifEmpty { title }
}
