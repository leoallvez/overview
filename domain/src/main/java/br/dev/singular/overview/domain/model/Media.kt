package br.dev.singular.overview.domain.model

class Media(
    val id: Long,
    val type: MediaType,
    val title: String,
    var isLiked: Boolean = false,
    val posterPath: String
)
