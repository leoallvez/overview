package br.dev.singular.overview.presentation.ui.components.user

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiUserAvatarSnapshotTest : UiSnapshotTest(snapshotPackage = "components/user") {

    @Test
    fun default() = snapshot {
        UiUserAvatarPreview()
    }
}
