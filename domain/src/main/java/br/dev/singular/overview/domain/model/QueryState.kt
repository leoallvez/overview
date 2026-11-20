package br.dev.singular.overview.domain.model

data class QueryState(
    val key: String = "",
    val type: MediaType = MediaType.UNKNOWN,
    val genre: Genre? = null,
    val catalog: Catalog? = null,
    val query: String = "",
    val page: Int = 0
)
