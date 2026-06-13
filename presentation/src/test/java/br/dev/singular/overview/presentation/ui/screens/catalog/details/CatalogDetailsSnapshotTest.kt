package br.dev.singular.overview.presentation.ui.screens.catalog.details

import br.dev.singular.overview.presentation.ui.components.UiScreenSnapshotTest
import org.junit.Test

class CatalogDetailsSnapshotTest : UiScreenSnapshotTest(snapshotPackage = "screens/catalog/details") {

    @Test
    fun default() = snapshot {
        CatalogDetailsContentPreview()
    }
}
