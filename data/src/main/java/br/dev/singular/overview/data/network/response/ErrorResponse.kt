package br.dev.singular.overview.data.network.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    var success: Boolean = false,
    var code: Long = 0,
    var message: String = ""
)
