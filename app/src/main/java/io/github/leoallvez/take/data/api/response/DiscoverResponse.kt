package io.github.leoallvez.take.data.api.response

import io.github.leoallvez.take.data.model.MediaItem

data class DiscoverResponse(
    val page: Int = 0,
    val results: List<MediaItem> = listOf()
)
