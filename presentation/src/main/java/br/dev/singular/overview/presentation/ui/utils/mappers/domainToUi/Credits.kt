package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.domain.model.Credits
import br.dev.singular.overview.presentation.model.CreditsUiModel

internal fun Credits.toUi() = CreditsUiModel(
    cast = cast.toUi(),
    crew = crew.toUi(),
)
