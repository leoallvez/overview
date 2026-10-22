package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.domain.model.Genre
import br.dev.singular.overview.presentation.model.GenreUiModel
import kotlinx.collections.immutable.toImmutableList

internal fun Genre.toUi() = GenreUiModel(
    id = id,
    name = name
)

internal fun List<Genre>.toUi() = map { it.toUi() }.toImmutableList()
