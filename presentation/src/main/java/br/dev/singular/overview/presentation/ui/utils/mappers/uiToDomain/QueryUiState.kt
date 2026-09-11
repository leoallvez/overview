package br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain

import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.presentation.model.QueryUiState

internal fun QueryUiState.toDomain() = QueryState(
    key = key,
    type = type.toDomain(),
    isLiked = isLiked,
    genre = genre?.toDomain(),
    catalog = catalog?.toDomain(),
    query = query,
    page = page
)
