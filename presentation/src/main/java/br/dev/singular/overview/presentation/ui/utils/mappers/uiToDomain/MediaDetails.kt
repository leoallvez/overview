package br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.PredictionInput
import br.dev.singular.overview.presentation.model.MediaDetailsUiModel
import java.util.Date

internal fun MediaDetailsUiModel.toMediaDomain(): Media {
    return with(metadata) {
        Media(
            id = id,
            type = type.toDomain(),
            title = title,
            isLiked = isLiked,
            posterPath = posterPath,
            lastUpdate = Date(),
            originalTitle = originalTitle,
            genres = originalGenres()
        )
    }
}

internal fun MediaDetailsUiModel.toDomain() = PredictionInput(
    id = metadata.id,
    type = type.toDomain(),
    title = metadata.originalTitle.ifEmpty { metadata.title },
    genres = originalGenres()
)

private fun MediaDetailsUiModel.originalGenres() = extras.genres.map {
    it.originalName.ifEmpty { it.name }
}
