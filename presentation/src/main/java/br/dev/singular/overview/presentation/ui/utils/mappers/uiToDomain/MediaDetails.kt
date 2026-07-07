package br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.presentation.model.MediaDetailsUiModel
import java.util.Date

fun MediaDetailsUiModel.toMediaDomain(): Media {
    return with(metadata) {
        Media(
            id = id,
            type = type.toDomain(),
            title = title,
            isLiked = isLiked,
            posterPath = posterPath,
            lastUpdate = Date()
        )
    }
}
