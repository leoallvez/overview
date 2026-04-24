package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.CatalogDataModel
import br.dev.singular.overview.domain.model.Catalog

internal fun CatalogDataModel.toDomain() = Catalog(
    id = id,
    name = name,
    priority = priority,
    logoPath = logoPath,
    display = display,
    lastUpdate = lastUpdate
)
