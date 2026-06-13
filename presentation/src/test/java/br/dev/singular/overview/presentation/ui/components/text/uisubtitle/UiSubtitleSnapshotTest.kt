package br.dev.singular.overview.presentation.ui.components.text.uisubtitle

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import br.dev.singular.overview.presentation.ui.components.text.UiSubtitlePreview
import org.junit.Test

class UiSubtitleSnapshotTest : UiSnapshotTest(snapshotPackage = "components/text/subtitle") {

    @Test
    fun default() = snapshot {
        UiSubtitlePreview()
    }
}
