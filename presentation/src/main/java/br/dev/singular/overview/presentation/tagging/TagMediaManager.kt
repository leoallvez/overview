package br.dev.singular.overview.presentation.tagging

object TagMediaManager {

    fun logClick(path: String, id: Long) = TagManager.logClick(path, "media-item", id)

    object Detail {
        const val SELECT_MEDIA_TYPE = "select-media-type:"
    }
}