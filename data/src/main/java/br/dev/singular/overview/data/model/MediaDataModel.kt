package br.dev.singular.overview.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.Date

@Serializable
data class MediaDataModel(
    var id: Long = 0,
    private val name: String = "",
    private val title: String = "",
    @SerialName("poster_path")
    val posterPath: String = "",
    @SerialName("media_type")
    val type: MediaDataType = MediaDataType.UNKNOWN,
    @Transient
    var isLiked: Boolean = false,
    @Transient
    var lastUpdate: Date = Date()
) {
    val betterTitle: String
        get() = when {
            name.isBlank().not() -> name
            title.isBlank().not() -> title
            else -> ""
        }
}
