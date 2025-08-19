package br.dev.singular.overview.domain.model

data class Filter (
    val page: Int = 0,
    val type: MediaType = MediaType.ALL
)