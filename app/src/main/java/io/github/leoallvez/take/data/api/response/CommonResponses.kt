package io.github.leoallvez.take.data.api.response

import com.squareup.moshi.Json

data class ListContentResponse<T>(
    val page: Long,
    val results: List<T>,

    @field:Json(name = "total_pages")
    val totalPages: Long,

    @field:Json(name = "total_results")
    val totalResults: Long
)

data class ContentResponse<T>(
    val page: Long,
    val results: List<T>,

    @field:Json(name = "total_pages")
    val totalPages: Long,

    @field:Json(name = "total_results")
    val totalResults: Long
)


data class ErrorResponse(
    var success: Boolean = false,

    @field:Json(name = "status_code")
    var statusCode: Long = 0,

    @field:Json(name = "status_message")
    var statusMessage: String = ""
)
