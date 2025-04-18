package br.dev.singular.overview.ui.model

import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.presentation.model.MediaType
import br.dev.singular.overview.presentation.model.MediaUIModel
import br.dev.singular.overview.data.source.media.MediaType as LegacyMediaType

//TODO: this file going to be delete in the future

fun Media.toUIModel(): MediaUIModel {
    return MediaUIModel(
        id = apiId,
        title = getLetter(),
        posterURLPath = getPosterImage(),
        type = getType().toNewMediaType()
    )
}

fun String.toNewMediaType() = when (this) {
    LegacyMediaType.MOVIE.key -> MediaType.MOVIE
    else -> MediaType.TV_SHOW
}

fun MediaType.toOldMediaType() = when (this) {
    MediaType.MOVIE -> LegacyMediaType.MOVIE.key
    else -> LegacyMediaType.MOVIE.key
}
