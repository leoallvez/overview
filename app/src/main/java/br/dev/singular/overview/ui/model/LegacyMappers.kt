package br.dev.singular.overview.ui.model

import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.presentation.model.MediaUIType
import br.dev.singular.overview.presentation.model.MediaUIModel
import br.dev.singular.overview.data.source.media.MediaType

//TODO: this file going to be delete in the future

fun Media.toUIModel(): MediaUIModel {
    return MediaUIModel(
        id = apiId,
        title = getLetter(),
        posterURLPath = getPosterImage(),
        type = getType().MediaUIType()
    )
}

fun String.MediaUIType() = when (this) {
    MediaType.MOVIE.key -> MediaUIType.MOVIE
    else -> MediaUIType.TV_SHOW
}

fun MediaUIType.toMediaType() = when (this) {
    MediaUIType.MOVIE -> MediaType.MOVIE.key
    else -> MediaType.MOVIE.key
}
