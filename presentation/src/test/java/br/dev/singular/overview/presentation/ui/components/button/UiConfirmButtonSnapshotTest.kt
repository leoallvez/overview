package br.dev.singular.overview.presentation.ui.components.button

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiConfirmButtonSnapshotTest : UiSnapshotTest(snapshotPackage = "components/button") {

    @Test
    fun enabled() = snapshot {
        UiConfirmButtonEnabledPreview()
    }

    @Test
    fun disabled() = snapshot {
        UiConfirmButtonDisabledPreview()
    }
}
