package br.com.deepbyte.overview.data.model.media

import com.squareup.moshi.Json

class Movie() : Media() {

    private val title: String? = null
    @field:Json(name = "original_title")
    private val originalTitle: String? = null

    @field:Json(name = "runtime")
    private val movieRuntime: Int = 0

    override fun getRuntime() = runtimeTemplate(runtime = movieRuntime)

    override fun getLetter() = title ?: originalTitle ?: ""
}
