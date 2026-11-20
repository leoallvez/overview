package br.dev.singular.overview.presentation.ui.components.catalog.item

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import br.dev.singular.overview.presentation.ui.components.catalog.UiCatalogItemPreview
import br.dev.singular.overview.presentation.ui.components.catalog.UiCatalogItemRadioButtonPreview
import org.junit.Test

class UiCatalogItemSnapshotTest : UiSnapshotTest(snapshotPackage = "components/catalog/item") {

    @Test
    fun default() = snapshot {
        UiCatalogItemPreview()
    }

    @Test
    fun radioButton() = snapshot {
        UiCatalogItemRadioButtonPreview()
    }
}
