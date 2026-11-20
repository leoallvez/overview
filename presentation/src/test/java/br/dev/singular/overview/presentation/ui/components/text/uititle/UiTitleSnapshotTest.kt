package br.dev.singular.overview.presentation.ui.components.text.uititle

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import br.dev.singular.overview.presentation.ui.components.text.UiTitlePreview
import org.junit.Test

class UiTitleSnapshotTest : UiSnapshotTest(snapshotPackage = "components/text/title") {

    @Test
    fun default() = snapshot {
        UiTitlePreview()
    }
}
