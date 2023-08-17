package br.com.deepbyte.overview.data.api.response

import br.com.deepbyte.overview.data.model.media.GenreEntity
import com.squareup.moshi.Json

class GenreListResponse {
    @field:Json(name = "genres")
    val genres: List<GenreEntity> = listOf()
}
