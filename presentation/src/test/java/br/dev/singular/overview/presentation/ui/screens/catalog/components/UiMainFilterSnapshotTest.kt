package br.dev.singular.overview.presentation.ui.screens.catalog.components

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiMainFilterSnapshotTest : UiSnapshotTest(snapshotPackage = "screens/catalog/components") {

    @Test
    fun default() = snapshot {
        UiMainFilterPreview()
    }
}
