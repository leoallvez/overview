package br.dev.singular.overview.presentation.ui.components.image

import br.dev.singular.overview.presentation.ui.components.UiImagePreview
import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiImageSnapshotTest : UiSnapshotTest(snapshotPackage = "components/image") {

    @Test
    fun default() = snapshot {
        UiImagePreview()
    }
}
