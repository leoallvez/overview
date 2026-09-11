package br.dev.singular.overview.presentation.ui.screens.favorites

import br.dev.singular.overview.presentation.ui.components.UiScreenSnapshotTest
import org.junit.Test

class FavoritesSnapshotTest : UiScreenSnapshotTest(snapshotPackage = "screens/favorites") {

    @Test
    fun default() = snapshot {
        FavoritesScreenPreview()
    }
}
