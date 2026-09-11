package br.dev.singular.overview.presentation.ui.components.button

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiActionButtonSnapshotTest : UiSnapshotTest(snapshotPackage = "components/button") {

    @Test
    fun enabledWithIcon() = snapshot {
        UiActionButtonEnabledWithIconPreview()
    }

    @Test
    fun enabledWithoutIcon() = snapshot {
        UiActionButtonEnabledWithoutIconPreview()
    }

    @Test
    fun disabledWithIcon() = snapshot {
        UiActionButtonDisabledWithIconPreview()
    }

    @Test
    fun disabledWithoutIcon() = snapshot {
        UiActionButtonDisabledWithoutIconPreview()
    }
}
