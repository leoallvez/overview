package br.dev.singular.overview.data.api.response

import br.dev.singular.overview.data.model.media.GenreEntity
import com.squareup.moshi.Json

class GenreListResponse {
    @field:Json(name = "genres")
    val genres: List<GenreEntity> = listOf()
}
