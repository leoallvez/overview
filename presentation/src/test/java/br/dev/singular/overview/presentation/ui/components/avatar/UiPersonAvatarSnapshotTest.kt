package br.dev.singular.overview.presentation.ui.components.avatar

import br.dev.singular.overview.presentation.ui.components.UiPersonAvatarPreview
import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiPersonAvatarSnapshotTest : UiSnapshotTest(snapshotPackage = "components/avatar") {

    @Test
    fun default() = snapshot {
        UiPersonAvatarPreview()
    }
}
