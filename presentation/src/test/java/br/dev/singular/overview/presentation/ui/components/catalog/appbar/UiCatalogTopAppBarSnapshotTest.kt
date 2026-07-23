package br.dev.singular.overview.presentation.ui.components.catalog.appbar

import br.dev.singular.overview.presentation.ui.components.UiScreenSnapshotTest
import br.dev.singular.overview.presentation.ui.components.catalog.UiCatalogTopAppBarCollapsedPreview
import br.dev.singular.overview.presentation.ui.components.catalog.UiCatalogTopAppBarLoadingPreview
import br.dev.singular.overview.presentation.ui.components.catalog.UiCatalogTopAppBarPreview
import org.junit.Test

class UiCatalogTopAppBarSnapshotTest : UiScreenSnapshotTest(snapshotPackage = "components/catalog/appbar") {

    @Test
    fun default() = snapshot {
        UiCatalogTopAppBarPreview()
    }

    @Test
    fun collapsed() = snapshot {
        UiCatalogTopAppBarCollapsedPreview()
    }

    @Test
    fun loading() = snapshot {
        UiCatalogTopAppBarLoadingPreview()
    }
}
