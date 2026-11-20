package br.dev.singular.overview.presentation.ui.screens.user.profile

import br.dev.singular.overview.presentation.ui.components.UiScreenSnapshotTest
import org.junit.Test

class ProfileSnapshotTest : UiScreenSnapshotTest(snapshotPackage = "screens/user/profile") {

    @Test
    fun default() = snapshot {
        ProfileScreenPreview()
    }

    @Test
    fun loading() = snapshot {
        ProfileScreenLoadingPreview()
    }

    @Test
    fun error() = snapshot {
        ProfileScreenErrorPreview()
    }
}
