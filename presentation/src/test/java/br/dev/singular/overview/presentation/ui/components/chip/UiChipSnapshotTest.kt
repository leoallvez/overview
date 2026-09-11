package br.dev.singular.overview.presentation.ui.components.chip

import br.dev.singular.overview.presentation.ui.components.UiChipActivatedPreview
import br.dev.singular.overview.presentation.ui.components.UiChipNotActivatedPreview
import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiChipSnapshotTest : UiSnapshotTest(snapshotPackage = "components/chip") {

    @Test
    fun activated() = snapshot {
        UiChipActivatedPreview()
    }

    @Test
    fun notActivated() = snapshot {
        UiChipNotActivatedPreview()
    }
}
