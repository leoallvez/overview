package io.github.leoallvez.take.data.api.response

import com.squareup.moshi.Json

data class ContentResponse<T>(
    val page: Long,
    val results: List<T>,

    @Json(name = "total_pages")
    val totalPages: Long,

    @Json(name = "total_results")
    val totalResults: Long
)

data class ErrorResponse(
    val success: Boolean,

    @Json(name = "status_code")
    val statusCode: Long,

    @Json(name = "status_message")
    val statusMessage: String
)