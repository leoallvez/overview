package br.dev.singular.overview.data.model

import androidx.room.Ignore
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowDetailsDataModel(
    val id: Long = 0,
    val name: String = "",
    @SerialName(value = "original_name")
    val originalName: String = "",
    @SerialName(value = "number_of_seasons")
    val numberOfSeasons: Int = 0,
    @SerialName(value = "number_of_episodes")
    val numberOfEpisodes: Int = 0,
    @SerialName(value = "episode_run_time")
    val episodeRuntime: List<Int> = listOf(),
    @SerialName(value = "first_air_date")
    val firstAirDate: String = "",
    @SerialName(value = "poster_path")
    val posterPath: String = "",
    @SerialName(value = "backdrop_path")
    val backdropPath: String = "",
    val overview: String = "",
    val genres: List<GenreDataModel> = emptyList(),
    @SerialName(value = "created_by")
    val creators: List<PersonDataModel> = emptyList(),
    val credits: CreditsDataModel = CreditsDataModel(),
    val videos: List<VideoDataModel> = emptyList(),
    val catalogs: List<CatalogDataModel> = emptyList(),
    val similar: MediaListDataModel = MediaListDataModel()
) {
    @get:Ignore
    val betterName: String
        get() = name.ifEmpty { originalName }
}
