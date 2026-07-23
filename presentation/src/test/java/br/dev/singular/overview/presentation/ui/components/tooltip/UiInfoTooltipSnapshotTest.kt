package br.dev.singular.overview.presentation.ui.components.tooltip

import br.dev.singular.overview.presentation.ui.components.UiInfoTooltipPreview
import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiInfoTooltipSnapshotTest : UiSnapshotTest(snapshotPackage = "components/tooltip") {

    @Test
    fun default() = snapshot {
        UiInfoTooltipPreview()
    }
}
