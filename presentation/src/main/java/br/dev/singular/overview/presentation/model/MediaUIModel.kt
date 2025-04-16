package br.dev.singular.overview.presentation.model

data class MediaUIModel(
    val id: Long,
    val contentType: ContentType,
    val title: String,
    val posterURLPath: String
)
