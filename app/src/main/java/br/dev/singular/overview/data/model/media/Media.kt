package br.dev.singular.overview.data.model.media

import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.presentation.BuildConfig
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.data.source.media.MediaType.*
import br.dev.singular.overview.presentation.model.MediaUiModel
import com.squareup.moshi.Json
import kotlinx.collections.immutable.ImmutableList

abstract class Media {
    @field:Json(name = "id")
    val apiId: Long = 0
    val overview: String = ""

    @field:Json(name = "backdrop_path")
    val backdropPath: String? = null

    @field:Json(name = "poster_path")
    private val posterPath: String? = null

    @field:Json(name = "original_language")
    val language: String? = null

    @field:Json(name = "vote_average")
    val voteAverage: Double = 0.0

    val genres: List<GenreEntity> = listOf()
    protected val credits: Credits = Credits()

    @field:Json(name = "providers")
    var streamings: List<StreamingEntity> = listOf()

    var videos: List<Video> = listOf()
    var isLiked: Boolean = false

    abstract fun getSimilarMedia(): ImmutableList<MediaUiModel>
    abstract fun getRuntime(): String
    abstract fun getLetter(): String
    abstract fun isReleased(): Boolean
    abstract fun getFormattedReleaseDate(): String

    fun getBackdropImage() = "${BuildConfig.IMG_URL}/$backdropPath"
    fun getPosterImage() = "${BuildConfig.IMG_URL}/$posterPath"
    fun getOrderedCast() = credits.cast.sortedBy { it.order }
    fun getType() = if (this is Movie) MOVIE.key else TV_SHOW.key

    protected fun runtimeTemplate(runtime: Int) = if (runtime > 0) {
        val hours = runtime / 60
        val minutes = runtime % 60
        if (hours > 0) "${hours}h ${if (minutes > 0) "${minutes}min" else ""}" else "$minutes min"
    } else {
        ""
    }

    fun toData() = MediaDataModel(
        id = apiId,
        posterPath = posterPath ?: "",
        name = getLetter(),
        title = getLetter(),
        isLiked = isLiked,
        type = MediaDataType.fromKey(getType())
    )
}
