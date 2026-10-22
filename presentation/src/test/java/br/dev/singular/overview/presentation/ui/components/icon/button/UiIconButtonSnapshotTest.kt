package br.dev.singular.overview.presentation.ui.components.icon.button

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import br.dev.singular.overview.presentation.ui.components.icon.UiIconButtonPainterPreview
import br.dev.singular.overview.presentation.ui.components.icon.UiIconButtonVectorPreview
import org.junit.Test

class UiIconButtonSnapshotTest : UiSnapshotTest(snapshotPackage = "components/icon/button") {

    @Test
    fun vector() = snapshot {
        UiIconButtonVectorPreview()
    }

    @Test
    fun painter() = snapshot {
        UiIconButtonPainterPreview()
    }
}
