package io.github.leoallvez.take.data.api.response

import io.github.leoallvez.take.data.model.MediaItem

data class DiscoverResponse(
    private val page: Int = 0,
    val results: List<MediaItem> = listOf()
) {
    fun prevPage() = if (page > 0) page.minus(other = 1) else null
    fun nextPage() = page.plus(other = 1)
}
