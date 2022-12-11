package br.com.deepbyte.overview.data.model.media

import com.squareup.moshi.Json

class TvShow : Media() {

    private val name: String? = null

    @field:Json(name = "original_name")
    private val originalName: String? = null

    @field:Json(name = "number_of_seasons")
    val numberOfSeasons: Int = 0

    @field:Json(name = "number_of_episodes")
    val numberOfEpisodes: Int = 0

    @field:Json(name = "episode_run_time")
    private val episodeRuntime: List<Int> = listOf()

    override fun getRuntime() = runtimeTemplate(runtime = episodeRuntime.average().toInt())

    override fun getLetter() = name ?: originalName ?: ""
}