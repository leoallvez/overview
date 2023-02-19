package br.com.deepbyte.overview.data.model.media

import br.com.deepbyte.overview.util.DateHelper
import com.squareup.moshi.Json

data class TvShow(
    private val name: String? = null,

    @field:Json(name = "original_name")
    private val originalName: String? = null,

    @field:Json(name = "number_of_seasons")
    val numberOfSeasons: Int = 0,

    @field:Json(name = "number_of_episodes")
    val numberOfEpisodes: Int = 0,

    @field:Json(name = "episode_run_time")
    private val episodeRuntime: List<Int> = listOf(),

    @field:Json(name = "first_air_date")
    private val firstAirDate: String = "",

    private val similar: Similar<TvShow> = Similar()
) : Media() {

    override fun getSimilarMedia() = similar.results

    override fun getRuntime() = runtimeTemplate(runtime = episodeRuntime.average().toInt())

    override fun getLetter() = name ?: originalName ?: ""
    override fun isReleased() = DateHelper(firstAirDate).isFutureDate().not()
    override fun getFormattedReleaseDate() = DateHelper(firstAirDate).formattedDate()
}
