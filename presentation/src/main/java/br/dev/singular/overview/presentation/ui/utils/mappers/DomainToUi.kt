package br.dev.singular.overview.presentation.ui.utils.mappers

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.presentation.BuildConfig
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.MediaUiType

fun List<Suggestion>.toUi() = associate { it.key to it.medias.map(Media::toUi) }

fun Media.toUi() = MediaUiModel(
    id = id,
    type = type.toUi(),
    title = title,
    isLiked = isLiked,
    posterURL = buildImageFullURL(posterPath)
)

internal fun MediaType.toUi() = when(this) {
    MediaType.MOVIE -> MediaUiType.MOVIE
    MediaType.TV -> MediaUiType.TV
    else -> MediaUiType.ALL
}

private fun buildImageFullURL(path: String) = "${BuildConfig.IMG_URL}$path}"
