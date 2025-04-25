package br.dev.singular.overview.data.model.media

import br.dev.singular.overview.presentation.BuildConfig

data class Video(
    val id: String,
    val name: String,
    val key: String,
    val site: String,
    val type: String,
    val official: Boolean,
    val publishedAt: String
) {
    val isValid: Boolean
        get() = site.lowercase() == "youtube" && key.isNotEmpty()

    fun getThumbnailImage(): String {
        return "${BuildConfig.THUMBNAIL_BASE_URL}/$key/${BuildConfig.THUMBNAIL_QUALITY}"
    }
}
