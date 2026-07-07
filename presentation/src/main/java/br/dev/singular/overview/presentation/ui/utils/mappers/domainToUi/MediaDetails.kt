package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.domain.model.MovieDetails
import br.dev.singular.overview.domain.model.TvShowDetails
import br.dev.singular.overview.presentation.model.MediaDetailsUiModel
import br.dev.singular.overview.presentation.model.MediaExtrasUiModel
import br.dev.singular.overview.presentation.model.MediaMetadataUiModel
import br.dev.singular.overview.presentation.ui.utils.DateHelper
import br.dev.singular.overview.presentation.ui.utils.mappers.buildImageFullURL
import br.dev.singular.overview.presentation.ui.utils.mappers.formatRuntime
import kotlinx.collections.immutable.toImmutableList

internal fun MovieDetails.toUi(isLiked: Boolean) = MediaDetailsUiModel.Movie(
    directors = directors.toImmutableList(),
    metadata = toMetadata(isLiked),
    credits = credits.toUi(),
    extras = toExtras(),
    durationFormatted = formatRuntime(runtime),
)

private fun MovieDetails.toExtras() = MediaExtrasUiModel(
    videos = videos.toUi(),
    genres = genres.toUi(),
    similar = similar.toUi(),
    catalogs = catalogs.toUi(),
)

internal fun TvShowDetails.toUi(isLiked: Boolean) = MediaDetailsUiModel.TvShow(
    creators = creators.toImmutableList(),
    metadata = toMetadata(isLiked),
    credits = credits.toUi(),
    extras = toExtras(),
    numberOfSeasons = numberOfSeasons,
    numberOfEpisodes = numberOfEpisodes,
    runtimePerEpisode = formatRuntime(episodeRuntime.average().toInt()),
)

private fun TvShowDetails.toExtras() = MediaExtrasUiModel(
    videos = videos.toUi(),
    genres = genres.toUi(),
    similar = similar.toUi(),
    catalogs = catalogs.toUi(),
)

private fun MovieDetails.toMetadata(isLiked: Boolean) = MediaMetadataUiModel(
    id = id,
    title = title,
    isLiked = isLiked,
    synopsis = overview,
    isReleased = !DateHelper(releaseDate).isFutureDate(),
    releaseDate = DateHelper(releaseDate).formattedDate(),
    backdropURL = buildImageFullURL(backdropPath),
    posterPath = posterPath,
    previewDrawableRes = null,
)

private fun TvShowDetails.toMetadata(isLiked: Boolean) = MediaMetadataUiModel(
    id = id,
    title = name,
    isLiked = isLiked,
    synopsis = overview,
    isReleased = !DateHelper(firstAirDate).isFutureDate(),
    releaseDate = DateHelper(firstAirDate).formattedDate(),
    backdropURL = buildImageFullURL(backdropPath),
    posterPath = posterPath,
    previewDrawableRes = null,
)
