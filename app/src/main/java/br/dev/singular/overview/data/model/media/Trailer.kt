package br.dev.singular.overview.data.model.media

import com.squareup.moshi.Json

data class Trailer(
    val id: String,
    val name: String,
    val key: String,
    val site: String,
    val type: String,
    val official: Boolean,
    @field:Json(name = "published_at")
    val publishedAt: String
) {
    val isValid: Boolean
        get() = site.lowercase() == "youTube" && type.lowercase() == "trailer" && official
}
