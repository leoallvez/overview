package br.dev.singular.overview.data.network.response

import kotlinx.serialization.Serializable

@Serializable
data class MapResponse<T>(
    val results: Map<String, T> = mapOf()
)
