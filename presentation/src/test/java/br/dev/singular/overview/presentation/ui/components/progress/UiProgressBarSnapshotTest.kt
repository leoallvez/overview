package br.dev.singular.overview.presentation.ui.components.progress

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiProgressBarSnapshotTest : UiSnapshotTest(snapshotPackage = "components/progress") {

    @Test
    fun default() = snapshot {
        UiProgressBarPreview()
    }
}
