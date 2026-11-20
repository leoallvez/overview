package br.dev.singular.overview.data.network.response

import kotlinx.serialization.Serializable

@Serializable
data class JevResponse(
    val answers: Map<String, JevAnswer> = emptyMap()
)

@Serializable
data class JevAnswer(
    val score: Double? = null,
    val confidence: Double? = null
)
