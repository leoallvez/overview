package br.dev.singular.overview.presentation.ui.screens.user.login

import br.dev.singular.overview.presentation.ui.components.UiScreenSnapshotTest
import org.junit.Test

class LoginSnapshotTest : UiScreenSnapshotTest(snapshotPackage = "screens/user/login") {

    @Test
    fun default() = snapshot {
        LoginScreenPreview()
    }

    @Test
    fun loading() = snapshot {
        LoginScreenLoadingPreview()
    }

    @Test
    fun error() = gif(
        duration = 6_000L
    ) {
        LoginScreenErrorPreview()
    }
}
