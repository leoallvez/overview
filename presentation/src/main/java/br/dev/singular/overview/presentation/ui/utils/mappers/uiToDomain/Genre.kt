package br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain

import br.dev.singular.overview.domain.model.Genre
import br.dev.singular.overview.presentation.model.GenreUiModel

internal fun GenreUiModel.toDomain() = Genre(
    id = id,
    name = name
)
