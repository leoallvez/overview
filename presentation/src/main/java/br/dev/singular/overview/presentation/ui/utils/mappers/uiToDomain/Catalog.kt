package br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain

import br.dev.singular.overview.domain.model.Catalog
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.ui.utils.mappers.extractPath
import java.util.Date

internal fun CatalogUiModel.toDomain() = Catalog(
    id = id,
    name = name,
    priority = priority,
    display = true,
    logoPath = extractPath(logoURL),
    lastUpdate = Date()
)
