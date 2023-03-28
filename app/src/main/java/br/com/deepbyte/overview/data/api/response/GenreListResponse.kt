package br.com.deepbyte.overview.data.api.response

import br.com.deepbyte.overview.data.model.media.Genre
import com.squareup.moshi.Json

class GenreListResponse {
    @field:Json(name = "genres")
    val genres: List<Genre> = listOf()
}
