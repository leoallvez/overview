package br.dev.singular.overview.presentation.ui.screens.person

import br.dev.singular.overview.presentation.ui.components.UiScreenSnapshotTest
import org.junit.Test

class PersonDetailsSnapshotTest : UiScreenSnapshotTest(snapshotPackage = "screens/person") {

    @Test
    fun default() = snapshot {
        PersonDetailsScreenPreview()
    }
}
