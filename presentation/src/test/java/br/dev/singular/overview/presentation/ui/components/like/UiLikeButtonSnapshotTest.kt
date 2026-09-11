package br.dev.singular.overview.presentation.ui.components.like

import br.dev.singular.overview.presentation.ui.components.UiLikeButtonActivePreview
import br.dev.singular.overview.presentation.ui.components.UiLikeButtonInactivePreview
import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiLikeButtonSnapshotTest : UiSnapshotTest(snapshotPackage = "components/like") {

    @Test
    fun active() = snapshot {
        UiLikeButtonActivePreview()
    }

    @Test
    fun inactive() = snapshot {
        UiLikeButtonInactivePreview()
    }
}
