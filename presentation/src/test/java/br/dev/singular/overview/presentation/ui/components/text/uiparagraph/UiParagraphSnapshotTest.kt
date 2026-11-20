package br.dev.singular.overview.presentation.ui.components.text.uiparagraph

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import br.dev.singular.overview.presentation.ui.components.text.UiParagraphPreview
import org.junit.Test

class UiParagraphSnapshotTest : UiSnapshotTest(snapshotPackage = "components/text/paragraph") {

    @Test
    fun default() = snapshot {
        UiParagraphPreview()
    }
}
