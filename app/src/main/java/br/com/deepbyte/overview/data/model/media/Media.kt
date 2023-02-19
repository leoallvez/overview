package br.com.deepbyte.overview.data.model.media

import br.com.deepbyte.overview.BuildConfig
import br.com.deepbyte.overview.data.MediaType
import br.com.deepbyte.overview.data.model.provider.StreamingService
import com.squareup.moshi.Json

abstract class Media {
    @field:Json(name = "id")
    val apiId: Long = 0
    val adult: Boolean = false
    val overview: String = ""
    @field:Json(name = "backdrop_path")
    private val backdropPath: String = ""
    @field:Json(name = "poster_path")
    private val posterPath: String = ""
    val genres: List<Genre> = listOf()
    protected val credits: Credits = Credits()
    @field:Json(name = "providers")
    var streamingServices: List<StreamingService> = listOf()

    abstract fun getSimilarMedia(): List<Media>
    abstract fun getRuntime(): String
    abstract fun getLetter(): String
    abstract fun isReleased(): Boolean
    abstract fun getFormattedReleaseDate(): String

    fun getBackdropImage() = "${BuildConfig.IMG_URL}/$backdropPath"
    fun getPosterImage() = "${BuildConfig.IMG_URL}/$posterPath"
    fun getOrderedCast() = credits.cast.sortedBy { it.order }
    fun getType() = if (this is Movie) MediaType.MOVIE.key else MediaType.TV_SHOW.key

    protected fun runtimeTemplate(runtime: Int) = if (runtime > 0) {
        val hours = runtime / 60
        val minutes = runtime % 60
        if (hours > 0) "${hours}h ${if (minutes > 0) "${minutes}min" else ""}" else "$minutes min"
    } else {
        ""
    }
}
