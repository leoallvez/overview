package br.dev.singular.overview.presentation.ui.components.navigation.bottom

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiBottomNavigationSnapshotTest : UiSnapshotTest(snapshotPackage = "components/navigation/bottom") {

    @Test
    fun default() = snapshot {
        UiBottomNavigationPreview()
    }
}
