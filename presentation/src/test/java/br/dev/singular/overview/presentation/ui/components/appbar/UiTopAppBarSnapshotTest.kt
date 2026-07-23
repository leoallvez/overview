package br.dev.singular.overview.presentation.ui.components.appbar

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import br.dev.singular.overview.presentation.ui.components.navigation.UiToolbarDefaultPreview
import br.dev.singular.overview.presentation.ui.components.navigation.UiToolbarWithCloseButtonPreview
import org.junit.Test

class UiTopAppBarSnapshotTest : UiSnapshotTest(snapshotPackage = "components/appbar") {

    @Test
    fun default() = snapshot {
        UiToolbarDefaultPreview()
    }

    @Test
    fun withCloseButton() = snapshot {
        UiToolbarWithCloseButtonPreview()
    }
}
