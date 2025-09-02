package br.dev.singular.overview.data.model

import br.dev.singular.overview.domain.model.MediaType
import kotlinx.serialization.Serializable

@Serializable
data class MediaRouteDataModel(
    val key: String,
    val path: String
) {

    fun withMediaType(type: MediaType): MediaRouteDataModel {
        return if (containsMediaType()) {
            copy(path = path.replace(MEDIA_TYPE_ALIAS, type.name.lowercase()))
        } else {
            this
        }
    }

    fun containsMediaType(): Boolean = path.contains(MEDIA_TYPE_ALIAS)

    companion object {
        private const val MEDIA_TYPE_ALIAS = "{media_type}"
    }
}
