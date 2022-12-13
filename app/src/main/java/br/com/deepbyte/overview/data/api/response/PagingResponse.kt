package br.com.deepbyte.overview.data.api.response

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
