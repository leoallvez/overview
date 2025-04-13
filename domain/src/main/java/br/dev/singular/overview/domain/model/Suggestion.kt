package br.dev.singular.overview.domain.model

data class Suggestion(
    val path: String,
    val order: Int,
    val type: String,
    val titleKey: String,
    val isActive: Boolean,
    val medias: List<Media> = emptyList()
)
