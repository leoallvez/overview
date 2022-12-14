package br.com.deepbyte.overview.data.api.response

import com.squareup.moshi.Json

data class ListResponse<T>(
    val page: Long = 0,
    val results: List<T> = listOf(),

    @field:Json(name = "total_pages")
    val totalPages: Long = 0,

    @field:Json(name = "total_results")
    val totalResults: Long = 0
)
