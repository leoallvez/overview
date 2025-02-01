package br.dev.singular.overview.data.model.media

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
        get() = site.lowercase() == "youtube"
}
