package br.dev.singular.overview.presentation.ui.components.scaffold

import br.dev.singular.overview.presentation.ui.components.UiScaffoldPreview
import br.dev.singular.overview.presentation.ui.components.UiScreenSnapshotTest
import org.junit.Test

class UiScaffoldSnapshotTest : UiScreenSnapshotTest(snapshotPackage = "components/scaffold") {

    @Test
    fun default() = snapshot {
        UiScaffoldPreview()
    }
}
