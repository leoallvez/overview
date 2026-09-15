package br.dev.singular.overview.presentation.ui.components.snackbar

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiSnackbarSnapshotTest : UiSnapshotTest(snapshotPackage = "components/snackbar") {

    @Test
    fun success() = snapshot {
        UiSnackbarSuccessPreview()
    }

    @Test
    fun error() = snapshot {
        UiSnackbarErrorPreview()
    }

    @Test
    fun customIcon() = snapshot {
        UiSnackbarCustomIconPreview()
    }
}
