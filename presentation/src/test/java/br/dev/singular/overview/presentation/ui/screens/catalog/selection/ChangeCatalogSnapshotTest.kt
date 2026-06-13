package br.dev.singular.overview.presentation.ui.screens.catalog.selection

import br.dev.singular.overview.presentation.ui.components.UiScreenSnapshotTest
import org.junit.Test

class ChangeCatalogSnapshotTest : UiScreenSnapshotTest(snapshotPackage = "screens/catalog/selection") {

    @Test
    fun success() = snapshot {
        ChangeCatalogsScreenSuccessPreview()
    }

    @Test
    fun loading() = snapshot {
        ChangeCatalogScreenLoadingPreview()
    }

    @Test
    fun error() = snapshot {
        ChangeCatalogScreenErrorPreview()
    }
}
