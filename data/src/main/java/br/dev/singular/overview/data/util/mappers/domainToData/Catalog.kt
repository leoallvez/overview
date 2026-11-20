package br.dev.singular.overview.data.util.mappers.domainToData

import br.dev.singular.overview.data.model.CatalogDataModel
import br.dev.singular.overview.domain.model.Catalog

internal fun Catalog.toData() = CatalogDataModel(
    id = id,
    name = name,
    priority = priority,
    logoPath = logoPath,
    display = display,
    lastUpdate = lastUpdate
)
