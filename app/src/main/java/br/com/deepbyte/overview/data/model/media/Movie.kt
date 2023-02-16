package br.com.deepbyte.overview.data.model.media

import br.com.deepbyte.overview.util.DateHelper
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

    override fun isReleased() = DateHelper(releaseDate).isFutureDate().not()

    fun getDirectorName(): String {
        val director = credits.crew.firstOrNull { it.job.uppercase() == DIRECTOR_JOB }
        return director?.name ?: ""
    }

    fun getReleaseYear() = DateHelper(releaseDate).getYear()

    companion object {
        private const val DIRECTOR_JOB = "DIRECTOR"
    }
}
