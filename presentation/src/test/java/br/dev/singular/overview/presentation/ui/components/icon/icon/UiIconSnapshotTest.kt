package br.dev.singular.overview.presentation.ui.components.icon.icon

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import br.dev.singular.overview.presentation.ui.components.icon.UiIconPreview
import org.junit.Test

class UiIconSnapshotTest : UiSnapshotTest(snapshotPackage = "components/icon/icon") {

    @Test
    fun default() = snapshot {
        UiIconPreview()
    }
}
