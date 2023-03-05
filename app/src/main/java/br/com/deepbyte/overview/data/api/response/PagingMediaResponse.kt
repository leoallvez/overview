package br.com.deepbyte.overview.data.api.response

import br.com.deepbyte.overview.data.model.media.Media

data class PagingMediaResponse<T : Media>(
    private val page: Int = 0,
    val results: List<T> = listOf()
) {
    fun prevPage() = if (page > 0) page.minus(other = FIRST_PAGE) else null

    fun nextPage() = page.plus(other = FIRST_PAGE)

    fun merge(other: PagingMediaResponse<T>): PagingResponse<T> {
        val newResults = results.plus(other.results).sortedByDescending { it.voteAverage }
        return PagingResponse(page, newResults)
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}
