package br.dev.singular.overview.data.network.response

data class ListResponse<T>(
    val results: List<T> = listOf()
)
