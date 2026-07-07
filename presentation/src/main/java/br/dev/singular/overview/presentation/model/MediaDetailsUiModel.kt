package br.dev.singular.overview.presentation.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class MediaMetadataUiModel(
    val id: Long,
    val title: String,
    val isLiked: Boolean,
    val synopsis: String,
    val isReleased: Boolean,
    val releaseDate: String,
    val posterPath: String,
    val backdropURL: String,
    @get:DrawableRes
    val previewDrawableRes: Int?,
)

@Immutable
data class CreditsUiModel(
    val crew: ImmutableList<PersonUiModel>,
    val cast: ImmutableList<PersonUiModel>,
)

@Immutable
data class MediaExtrasUiModel(
    val videos: ImmutableList<VideoUiModel>,
    val genres: ImmutableList<GenreUiModel>,
    val similar: ImmutableList<MediaUiModel>,
    val catalogs: ImmutableList<CatalogUiModel>,
)

@Immutable
sealed interface MediaDetailsUiModel {

    val metadata: MediaMetadataUiModel
    val credits: CreditsUiModel
    val extras: MediaExtrasUiModel
    val type: MediaUiType

    @Immutable
    data class Movie(
        override val metadata: MediaMetadataUiModel,
        override val credits: CreditsUiModel,
        override val extras: MediaExtrasUiModel,
        val durationFormatted: String,
        val directors: ImmutableList<String>,
        override val type: MediaUiType = MediaUiType.MOVIE
    ) : MediaDetailsUiModel

    @Immutable
    data class TvShow(
        override val metadata: MediaMetadataUiModel,
        override val credits: CreditsUiModel,
        override val extras: MediaExtrasUiModel,
        val numberOfSeasons: Int,
        val numberOfEpisodes: Int,
        val creators: ImmutableList<String>,
        val runtimePerEpisode: String,
        override val type: MediaUiType = MediaUiType.TV
    ) : MediaDetailsUiModel
}
