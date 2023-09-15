package dev.com.singular.overview.data.api.response

import com.squareup.moshi.Json

data class ErrorResponse(
    var success: Boolean = false,

    @field:Json(name = "status_code")
    var code: Long = 0,

    @field:Json(name = "status_message")
    var message: String = ""
)
