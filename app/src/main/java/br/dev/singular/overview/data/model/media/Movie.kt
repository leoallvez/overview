package br.dev.singular.overview.data.model.media

import br.dev.singular.overview.util.DateHelper
import com.squareup.moshi.Json

data class Movie(
    private val title: String? = null,

    @field:Json(name = "original_title")
    private val originalTitle: String? = null,

    @field:Json(name = "runtime")
    private val movieRuntime: Int = 0,

    @field:Json(name = "release_date")
    private val releaseDate: String = "",

    private val similar: Similar<Movie> = Similar()
) : Media() {

    override fun getSimilarMedia() = similar.results

    override fun getRuntime() = runtimeTemplate(runtime = movieRuntime)

    override fun getLetter() = title ?: originalTitle ?: ""

    fun getDirectorName(): String {
        val director = credits.crew.firstOrNull { it.job.uppercase() == DIRECTOR_JOB }
        return director?.name ?: ""
    }

    override fun isReleased() = DateHelper(releaseDate).isFutureDate().not()

    override fun getFormattedReleaseDate() = DateHelper(releaseDate).formattedDate()

    companion object {
        private const val DIRECTOR_JOB = "DIRECTOR"
    }
}
