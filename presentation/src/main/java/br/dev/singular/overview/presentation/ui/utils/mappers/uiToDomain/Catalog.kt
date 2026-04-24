package br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain

import br.dev.singular.overview.domain.model.Catalog
import br.dev.singular.overview.presentation.model.CatalogUiModel
import java.util.Date

internal fun CatalogUiModel.toDomain() = Catalog(
    id = id,
    name = name,
    priority = priority,
    display = true,
    // TODO: create a logic to get the logo path
    logoPath = logoURL,
    lastUpdate = Date()
)
