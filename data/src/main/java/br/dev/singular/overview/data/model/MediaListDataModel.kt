package br.dev.singular.overview.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MediaListDataModel(
    val results: List<MediaDataModel> = listOf()
)
