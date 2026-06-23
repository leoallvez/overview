package br.dev.singular.overview.data.api.response

data class ListResponse<T>(
    val results: List<T> = listOf(),
)
