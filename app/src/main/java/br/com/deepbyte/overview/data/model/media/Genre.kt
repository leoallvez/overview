package br.com.deepbyte.overview.data.model.media

import br.com.deepbyte.overview.data.api.response.DataResponse
import br.com.deepbyte.overview.data.api.response.MediaDetailResponse
import br.com.deepbyte.overview.data.model.DiscoverParams

data class Genre(val name: String = "") : DataResponse() {

    fun createDiscoverParams(
        media: MediaDetailResponse
    ) = DiscoverParams(
        apiId = apiId,
        screenTitle = name,
        mediaId = media.apiId,
        mediaType = media.type ?: ""
    )
}