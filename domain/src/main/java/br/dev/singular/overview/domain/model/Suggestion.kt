package br.dev.singular.overview.domain.model

data class Suggestion(
    val order: Int,
    val key: String,
    val type: MediaType,
    val isActive: Boolean,
    val medias: List<Media> = emptyList()
)
