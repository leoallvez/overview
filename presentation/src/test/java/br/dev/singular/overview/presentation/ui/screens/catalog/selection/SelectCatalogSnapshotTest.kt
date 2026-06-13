package br.dev.singular.overview.presentation.ui.screens.catalog.selection

import br.dev.singular.overview.presentation.ui.components.UiScreenSnapshotTest
import org.junit.Test

class SelectCatalogSnapshotTest : UiScreenSnapshotTest(snapshotPackage = "screens/catalog/selection") {

    @Test
    fun success() = snapshot {
        SelectCatalogsScreenSuccessPreview()
    }

    @Test
    fun loading() = snapshot {
        SelectCatalogScreenLoadingPreview()
    }

    @Test
    fun error() = snapshot {
        SelectCatalogScreenErrorPreview()
    }
}
