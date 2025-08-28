package br.dev.singular.overview.presentation.tagging

import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.tagging.TagMediaManager.Detail.SELECT_MEDIA_TYPE

object TagMediaManager {

    fun logMediaClick(path: String, id: Long) =
        TagManager.logClick(path, "media-item", id)

    fun logTypeClick(path: String, type: MediaUiType) {
        TagManager.logClick(path, "${SELECT_MEDIA_TYPE}${type.name.lowercase()}")
    }

    private object Detail {
        const val SELECT_MEDIA_TYPE = "select-media-type:"
    }
}