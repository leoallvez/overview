package br.dev.singular.overview.presentation.ui.components.column

import br.dev.singular.overview.presentation.ui.components.UiCenteredColumnPreview
import br.dev.singular.overview.presentation.ui.components.UiScreenSnapshotTest
import org.junit.Test

class UiCenteredColumnSnapshotTest : UiScreenSnapshotTest(snapshotPackage = "components/column") {

    @Test
    fun default() = snapshot {
        UiCenteredColumnPreview()
    }
}
