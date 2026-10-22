package br.dev.singular.overview.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MediaRouteDataModel(
    val key: String,
    val path: String,
    val params: Map<String, String> = emptyMap()
)
