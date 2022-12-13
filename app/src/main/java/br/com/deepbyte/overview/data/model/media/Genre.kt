package br.com.deepbyte.overview.data.model.media

import br.com.deepbyte.overview.data.MediaType
import br.com.deepbyte.overview.data.model.DiscoverParams
import com.squareup.moshi.Json

data class Genre(
    @field:Json(name = "id")
    val apiId: Long = 0,
    val name: String = ""
) {

    fun createDiscoverParams(
        media: Media
    ) = DiscoverParams(
        apiId = apiId,
        screenTitle = name,
        mediaId = media.apiId,
        mediaType = if (media is Movie) MediaType.MOVIE.key else MediaType.TV.key
    )
}
