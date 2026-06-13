package br.dev.singular.overview.presentation.ui.components.catalog.card

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import br.dev.singular.overview.presentation.ui.components.catalog.UiCatalogCardPreview
import org.junit.Test

class UiCatalogCardSnapshotTest : UiSnapshotTest(snapshotPackage = "components/catalog/card") {

    @Test
    fun default() = snapshot {
        UiCatalogCardPreview()
    }
}
