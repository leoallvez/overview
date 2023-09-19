package br.dev.singular.overview.data.api.response

import com.squareup.moshi.Json
import br.dev.singular.overview.data.model.media.GenreEntity

class GenreListResponse {
    @field:Json(name = "genres")
    val genres: List<GenreEntity> = listOf()
}
