package br.dev.singular.overview.presentation.ui.components.text.uitext

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import br.dev.singular.overview.presentation.ui.components.text.UiTextPreview
import br.dev.singular.overview.presentation.ui.components.text.UiTextSkeletonPreview
import org.junit.Test

class UiTextSnapshotTest : UiSnapshotTest(snapshotPackage = "components/text") {

    @Test
    fun default() = snapshot {
        UiTextPreview()
    }

    @Test
    fun skeleton() = snapshot {
        UiTextSkeletonPreview()
    }
}
