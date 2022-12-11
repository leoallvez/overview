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

data class PagingResponse<T>(
    private val page: Int = 0,
    val results: List<T> = listOf()
) {
    fun prevPage() = if (page > 0) page.minus(other = FIRST_PAGE) else null
    fun nextPage() = page.plus(other = FIRST_PAGE)

    companion object {
        private const val FIRST_PAGE = 1
    }
}

data class ErrorResponse(
    var success: Boolean = false,

    @field:Json(name = "status_code")
    var code: Long = 0,

    @field:Json(name = "status_message")
    var message: String = ""
)
