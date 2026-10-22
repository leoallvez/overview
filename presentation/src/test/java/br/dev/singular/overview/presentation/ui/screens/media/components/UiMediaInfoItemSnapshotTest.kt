package br.dev.singular.overview.presentation.ui.screens.media.components

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiMediaInfoItemSnapshotTest : UiSnapshotTest(snapshotPackage = "screens/media/components") {

    @Test
    fun default() = snapshot {
        UiMediaInfoItemPreview()
    }
}
