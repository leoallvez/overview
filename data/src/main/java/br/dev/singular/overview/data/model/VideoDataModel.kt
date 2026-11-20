package br.dev.singular.overview.data.model

import kotlinx.serialization.Serializable

@Serializable
data class VideoDataModel(
    val id: String,
    val key: String,
    val name: String,
)
