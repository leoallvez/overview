package br.dev.singular.overview.presentation.ui.screens.genre

import br.dev.singular.overview.presentation.ui.components.UiScreenSnapshotTest
import org.junit.Test

class SelectGenreSnapshotTest : UiScreenSnapshotTest(snapshotPackage = "screens/genre") {

    @Test
    fun success() = snapshot {
        SelectGenreScreenSuccessPreview()
    }

    @Test
    fun loading() = snapshot {
        SelectGenreScreenLoadingPreview()
    }

    @Test
    fun error() = snapshot {
        SelectGenreScreenErrorPreview()
    }
}
