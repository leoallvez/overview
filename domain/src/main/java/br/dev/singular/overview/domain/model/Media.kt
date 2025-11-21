package br.dev.singular.overview.domain.model

import java.util.Date

data class Media(
    val id: Long,
    val type: MediaType,
    val title: String,
    var isLiked: Boolean,
    val posterPath: String,
    val lastUpdate: Date
)
