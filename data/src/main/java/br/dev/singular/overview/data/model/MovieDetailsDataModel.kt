package br.dev.singular.overview.data.model

import androidx.room.Ignore
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsDataModel(
    val id: Long = 0,
    val title: String = "",
    @SerialName(value = "original_title")
    val originalTitle: String = "",
    @SerialName(value = "release_date")
    val releaseDate: String = "",
    val runtime: Int = 0,
    @SerialName(value = "poster_path")
    val posterPath: String = "",
    @SerialName(value = "backdrop_path")
    val backdropPath: String = "",
    val overview: String = "",
    val genres: List<GenreDataModel> = emptyList(),
    val credits: CreditsDataModel = CreditsDataModel(),
    val videos: List<VideoDataModel> = emptyList(),
    val catalogs: List<CatalogDataModel> = emptyList(),
    val similar: MediaListDataModel = MediaListDataModel()
) {
    @get:Ignore
    val betterTitle: String
        get() = title.ifEmpty { originalTitle }
}
