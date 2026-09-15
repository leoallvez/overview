package br.dev.singular.overview.presentation.ui.components.button

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiGoogleButtonSnapshotTest : UiSnapshotTest(snapshotPackage = "components/button") {

    @Test
    fun default() = snapshot {
        UiGoogleButtonPreview()
    }
}
