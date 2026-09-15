package br.dev.singular.overview.presentation.ui.components.tooltip

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiTooltipSnapshotTest : UiSnapshotTest(snapshotPackage = "components/tooltip") {

    @Test
    fun default() = snapshot {
        UiTooltipPreview()
    }

    @Test
    fun withoutCloseIcon() = snapshot {
        UiTooltipWithoutIconPreview()
    }

    @Test
    fun customIcon() = snapshot {
        UiTooltipWithCustomIconPreview()
    }

    @Test
    fun animation() = gif {
        UiTooltipAnimationPreview()
    }
}
