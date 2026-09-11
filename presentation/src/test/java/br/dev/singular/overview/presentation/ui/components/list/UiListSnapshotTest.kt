package br.dev.singular.overview.presentation.ui.components.list

import br.dev.singular.overview.presentation.ui.components.UiListPreview
import br.dev.singular.overview.presentation.ui.components.UiScreenSnapshotTest
import org.junit.Test

class UiListSnapshotTest : UiScreenSnapshotTest(snapshotPackage = "components/list") {

    @Test
    fun default() = snapshot {
        UiListPreview()
    }
}
