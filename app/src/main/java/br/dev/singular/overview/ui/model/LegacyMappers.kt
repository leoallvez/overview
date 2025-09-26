package br.dev.singular.overview.ui.model

import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.data.source.media.MediaType

//TODO: this file going to be delete in the future

fun Media.toUIModel(): MediaUiModel {
    return MediaUiModel(
        id = apiId,
        title = getLetter(),
        posterURL = getPosterImage(),
        type = getType().MediaUIType()
    )
}

fun String.MediaUIType() = when (this) {
    MediaType.MOVIE.key -> MediaUiType.MOVIE
    else -> MediaUiType.TV
}

fun MediaUiType.toMediaType() = when (this) {
    MediaUiType.MOVIE -> MediaType.MOVIE.key
    else -> MediaType.TV_SHOW.key
}
