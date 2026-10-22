package br.dev.singular.overview.presentation.ui.components.divider

import br.dev.singular.overview.presentation.ui.components.UiDividerPreview
import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiDividerSnapshotTest : UiSnapshotTest(snapshotPackage = "components/divider") {

    @Test
    fun default() = snapshot {
        UiDividerPreview()
    }
}
