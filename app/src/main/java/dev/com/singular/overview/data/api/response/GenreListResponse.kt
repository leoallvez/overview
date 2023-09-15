package dev.com.singular.overview.data.api.response

import com.squareup.moshi.Json
import dev.com.singular.overview.data.model.media.GenreEntity

class GenreListResponse {
    @field:Json(name = "genres")
    val genres: List<GenreEntity> = listOf()
}
