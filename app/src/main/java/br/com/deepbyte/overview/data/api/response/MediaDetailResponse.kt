package br.com.deepbyte.overview.data.api.response

import br.com.deepbyte.overview.BuildConfig.IMG_URL
import br.com.deepbyte.overview.data.model.DiscoverParams
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.data.model.provider.ProviderPlace
import br.com.deepbyte.overview.util.DateHelper
import com.squareup.moshi.Json

data class MediaDetailResponse(
    @field:Json(name = "backdrop_path")
    private val backdropPath: String = "",

    @field:Json(name = "original_title")
    private val originalTitle: String? = null,

    @field:Json(name = "original_name")
    private val originalName: String? = null,

    val overview: String = "",

    @field:Json(name = "poster_path")
    private val posterPath: String? = "",

    @field:Json(name = "release_date")
    private val releaseDate: String = "",

    @field:Json(name = "number_of_seasons")
    val numberOfSeasons: Int = 0,

    @field:Json(name = "number_of_episodes")
    val numberOfEpisodes: Int = 0,

    @field:Json(name = "runtime")
    private val movieRuntime: Int = 0,

    @field:Json(name = "episode_run_time")
    private val episodeRuntime: List<Int> = listOf(),

    private val title: String? = null,
    private val name: String? = null,

    var type: String? = null,

    val genres: List<Genre> = listOf(),
    private val credits: Credits = Credits(),
    val similar: Similar = Similar(),

    var providers: List<ProviderPlace> = listOf()
) : DataResponse() {

    private val mediaTitle: String?
        get() = title ?: originalTitle

    private val mediaName: String
        get() = name ?: originalName ?: ""

    fun getLetter() = mediaTitle ?: mediaName

    fun getPosterImage() = "$IMG_URL/$posterPath"

    fun getBackdropImage() = "$IMG_URL/$backdropPath"

    fun getOrderedCast() = credits.cast.sortedBy { it.order }

    fun getDirectorName(): String {
        val director = credits.crew.firstOrNull { it.job.uppercase() == DIRECTOR_JOB }
        return director?.name ?: ""
    }

    fun getReleaseYear() = DateHelper(releaseDate).getYear()

    fun getEpisodesRuntime() = runtimeTemplate(runtime = episodeRuntime.average().toInt())

    fun getMovieRuntime() = runtimeTemplate(runtime = movieRuntime)

    private fun runtimeTemplate(runtime: Int) = if (runtime > 0) {
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

data class Credits(
    val cast: List<PersonDetails> = listOf(),
    val crew: List<PersonDetails> = listOf()
)

data class Genre(val name: String = "") : DataResponse() {

    fun createDiscoverParams(
        media: MediaDetailResponse
    ) = DiscoverParams(
        apiId = apiId,
        screenTitle = name,
        mediaId = media.apiId,
        mediaType = media.type ?: ""
    )
}

data class Similar(val results: List<MediaItem> = listOf())
