package br.dev.singular.overview.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QueryDataState(
    @SerialName("path")
    val path: String,
    @SerialName("type")
    val type: MediaDataType = MediaDataType.UNKNOWN,
    @SerialName("genre")
    val genre: GenreDataModel? = null,
    @SerialName("catalog")
    val catalog: CatalogDataModel? = null,
    @SerialName("is_liked")
    val isLiked: Boolean = false,
    @SerialName("query")
    val query: String = "",
    @SerialName("page")
    val page: Int = 0
)

