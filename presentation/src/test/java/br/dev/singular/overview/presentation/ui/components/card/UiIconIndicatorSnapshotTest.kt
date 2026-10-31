package br.dev.singular.overview.presentation.ui.components.card

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiIconIndicatorSnapshotTest : UiSnapshotTest(snapshotPackage = "components/card") {

    @Test
    fun default() = snapshot {
        UiIconIndicatorPreview()
    }
}
