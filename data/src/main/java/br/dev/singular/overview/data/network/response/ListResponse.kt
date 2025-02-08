package br.dev.singular.overview.data.network.response

import kotlinx.serialization.Serializable

@Serializable
data class ListResponse<T>(
    val page: Long = 0,
    val results: List<T> = listOf(),
    val totalPages: Long = 0,
    val totalResults: Long = 0
)
