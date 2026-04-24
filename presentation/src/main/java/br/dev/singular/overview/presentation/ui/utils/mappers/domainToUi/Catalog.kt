package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.domain.model.Catalog
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.ui.utils.mappers.buildImageFullURL
import kotlinx.collections.immutable.toImmutableList

internal fun List<Catalog>.toUi() = map(Catalog::toUi).toImmutableList()

internal fun Catalog.toUi() = CatalogUiModel(
    id = id,
    name = name,
    priority = priority,
    previewDrawableRes = null,
    logoURL = buildImageFullURL(logoPath)
)
