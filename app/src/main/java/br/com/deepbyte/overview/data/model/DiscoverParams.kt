package br.com.deepbyte.overview.data.model

import br.com.deepbyte.overview.data.api.response.Genre
import br.com.deepbyte.overview.data.api.response.MediaDetailResponse as Media
import br.com.deepbyte.overview.data.api.response.ProviderPlace as Provider
import com.google.gson.Gson

data class DiscoverParams(
    val apiId: Long = 0,
    val screenTitle: String = "",
    val mediaId: Long = 0,
    val mediaType: String = ""
) {
    fun toJson(): String = Gson().toJson(this)

    companion object {

        fun create(provider: Provider, media: Media) = DiscoverParams(
            apiId = provider.apiId,
            screenTitle = provider.providerName,
            mediaId = media.apiId,
            mediaType = media.type ?: ""
        )

        fun create(genre: Genre, media: Media) = DiscoverParams(
            apiId = genre.apiId,
            screenTitle = genre.name,
            mediaId = media.apiId,
            mediaType = media.type ?: ""
        )
    }
}
