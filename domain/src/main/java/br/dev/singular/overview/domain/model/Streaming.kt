package br.dev.singular.overview.domain.model

import java.util.Date

data class Streaming(
    var id: Long,
    val name: String,
    val priority: Int,
    val logoPath: String,
    var display: Boolean,
    var lastUpdate: Date
)
