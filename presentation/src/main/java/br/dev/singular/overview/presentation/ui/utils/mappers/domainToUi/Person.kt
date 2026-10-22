package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.domain.model.Person
import br.dev.singular.overview.presentation.model.PersonUiModel
import br.dev.singular.overview.presentation.ui.utils.mappers.buildPosterURL
import kotlinx.collections.immutable.toImmutableList

internal fun Person.toUi() = PersonUiModel(
    id = id,
    name = name,
    description = character.ifBlank { job },
    previewDrawableRes = null,
    profileURL = buildPosterURL(profilePath),
)

internal fun List<Person>.toUi() = map { it.toUi() }.toImmutableList()
