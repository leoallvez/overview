package br.dev.singular.overview.ui.model

import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.presentation.model.MediaType
import br.dev.singular.overview.data.source.media.MediaType as OldMediaType
import br.dev.singular.overview.presentation.model.MediaUIModel

//TODO: this file going to be delete in the future

fun Media.toUIModel(): MediaUIModel {
    return MediaUIModel(
        id = apiId,
        title = getLetter(),
        posterURLPath = getPosterImage(),
        mediaType = if (getType() == OldMediaType.MOVIE.key) {
            MediaType.MOVIE
        } else {
            MediaType.TV_SHOW
        }
    )
}
