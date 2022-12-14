package br.com.deepbyte.overview.data.model.media

import com.squareup.moshi.Json

data class Genre(
    @field:Json(name = "id")
    val apiId: Long = 0,
    val name: String = ""
)
