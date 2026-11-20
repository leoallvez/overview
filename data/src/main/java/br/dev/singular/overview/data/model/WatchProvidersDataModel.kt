package br.dev.singular.overview.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WatchProvidersDataModel(
    @SerialName(value = "flatrate")
    private val flatRate: List<CatalogDataModel> = emptyList()
) {
    val flatRatesByPriority: List<CatalogDataModel>
        get() = flatRate.sortedBy { it.priority }

}
