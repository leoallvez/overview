package br.dev.singular.overview.domain.model

import java.util.Date

data class Suggestion(
    val id: Long = 0,
    val order: Int,
    val key: String,
    val type: MediaType,
    val isActive: Boolean,
    val medias: List<Media> = emptyList(),
    val lastUpdate: Date
)
