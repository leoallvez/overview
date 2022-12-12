package br.com.deepbyte.overview.data.model.media

import br.com.deepbyte.overview.data.api.response.MediaDetailResponse
import br.com.deepbyte.overview.data.model.DiscoverParams
import com.squareup.moshi.Json

data class Genre(
    @field:Json(name = "id")
    val apiId: Long = 0,
    val name: String = ""
) {

    fun createDiscoverParams(
        media: MediaDetailResponse
    ) = DiscoverParams(
        apiId = apiId,
        screenTitle = name,
        mediaId = media.apiId,
        mediaType = media.type ?: ""
    )
}
