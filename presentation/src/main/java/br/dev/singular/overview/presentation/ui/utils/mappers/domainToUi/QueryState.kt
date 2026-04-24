package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.presentation.model.QueryUiState

fun QueryState.toUi() = QueryUiState(
    key = key,
    type = type.toUi(),
    isLiked = isLiked,
    catalog = catalog?.toUi(),
    genre = genre?.toUi(),
    query = query,
    page = page
)
