package br.com.deepbyte.overview.data.api.response

import com.squareup.moshi.Json

data class ListContentResponse<T>(
    val page: Long = 0,
    val results: List<T> = listOf(),

    @field:Json(name = "total_pages")
    val totalPages: Long = 0,

    @field:Json(name = "total_results")
    val totalResults: Long = 0
)

data class ErrorResponse(
    var success: Boolean = false,

    @field:Json(name = "status_code")
    var code: Long = 0,

    @field:Json(name = "status_message")
    var message: String = ""
)
