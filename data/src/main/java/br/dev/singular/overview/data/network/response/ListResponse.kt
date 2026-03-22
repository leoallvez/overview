package br.dev.singular.overview.data.network.response

import kotlinx.serialization.Serializable

@Serializable
data class ListResponse<T>(
    val results: List<T> = listOf()
)
