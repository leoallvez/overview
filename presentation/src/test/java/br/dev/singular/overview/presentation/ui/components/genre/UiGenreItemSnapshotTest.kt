package br.dev.singular.overview.presentation.ui.components.genre

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiGenreItemSnapshotTest : UiSnapshotTest(snapshotPackage = "components/genre") {

    @Test
    fun default() = snapshot {
        UiGenreItemPreview()
    }
}
