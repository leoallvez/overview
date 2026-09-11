package br.dev.singular.overview.data.network.response

import br.dev.singular.overview.data.model.GenreDataModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GenreListResponse {
    @SerialName("genres")
    val genres: List<GenreDataModel> = listOf()
}
