package br.com.deepbyte.overview.data.model.media

import br.com.deepbyte.overview.BuildConfig
import br.com.deepbyte.overview.data.model.provider.ProviderPlace
import com.squareup.moshi.Json

abstract class Media {

    val apiId: Long = 0
    val overview: String = ""

    @field:Json(name = "backdrop_path")
    private val backdropPath: String = ""

    @field:Json(name = "poster_path")
    private val posterPath: String? = ""

    @field:Json(name = "release_date")
    private val releaseDate: String = ""

    val genres: List<Genre> = listOf()
    private val credits: Credits = Credits()
    val similar: Similar = Similar()

    var providers: List<ProviderPlace> = listOf()

    fun getBackdropImage() = "${BuildConfig.IMG_URL}/$backdropPath"
    fun getOrderedCast() = credits.cast.sortedBy { it.order }

    abstract fun getRuntime(): String
    abstract fun getLetter(): String

    protected fun runtimeTemplate(runtime: Int) = if (runtime > 0) {
        val hours = runtime / 60
        val minutes = runtime % 60
        if (hours > 0) "${hours}h ${if (minutes > 0) "${minutes}min" else ""}" else "$minutes min"
    } else {
        ""
    }

    companion object {
        private const val DIRECTOR_JOB = "DIRECTOR"
    }
}
