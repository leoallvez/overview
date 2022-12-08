package br.com.deepbyte.overview.data.api.response

import br.com.deepbyte.overview.data.model.MediaItem

data class SearchMediaResponse(
    private val page: Int = 0,
    val results: List<MediaItem> = listOf()
)
