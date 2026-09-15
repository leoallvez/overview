package br.dev.singular.overview.presentation.ui.components.dialog

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiAlertDialogSnapshotTest : UiSnapshotTest(snapshotPackage = "components/dialog") {

    @Test
    fun default() = snapshot {
        UiAlertDialogPreview()
    }

    @Test
    fun longText() = snapshot {
        UiAlertDialogLongTextPreview()
    }
}
