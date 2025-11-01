package br.dev.singular.overview.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias MediaDataPage = DataPage<MediaDataModel>

@Serializable
data class DataPage<T>(
    @SerialName("results")
    val items: List<T> = listOf(),
    val page: Int = 0,
    val isLastPage: Boolean = false
)
