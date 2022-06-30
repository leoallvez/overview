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
    val success: Boolean,

    @field:Json(name = "status_code")
    val statusCode: Long,

    @field:Json(name = "status_message")
    val statusMessage: String
)
