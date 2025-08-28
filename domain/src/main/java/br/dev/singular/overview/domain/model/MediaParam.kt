package br.dev.singular.overview.domain.model

data class MediaParam(
    val key: String = "",
    val type: MediaType = MediaType.UNKNOWN,
    val isLiked: Boolean = false,
    val query: String = "",
    val page: Int = 0
)
