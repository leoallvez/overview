package br.dev.singular.overview.presentation.ui.components.media.grid

import br.dev.singular.overview.presentation.ui.components.UiScreenSnapshotTest
import br.dev.singular.overview.presentation.ui.components.media.UiMediaGridHorizontalPreview
import br.dev.singular.overview.presentation.ui.components.media.UiMediaGridSkeletonHorizontalPreview
import br.dev.singular.overview.presentation.ui.components.media.UiMediaGridSkeletonPreview
import br.dev.singular.overview.presentation.ui.components.media.UiMediaGridVerticalPreview
import org.junit.Test

class UiMediaGridSnapshotTest : UiScreenSnapshotTest(snapshotPackage = "components/media/grid") {

    @Test
    fun vertical() = snapshot {
        UiMediaGridVerticalPreview()
    }

    @Test
    fun horizontal() = snapshot {
        UiMediaGridHorizontalPreview()
    }

    @Test
    fun skeleton() = snapshot {
        UiMediaGridSkeletonPreview()
    }

    @Test
    fun skeletonHorizontal() = snapshot {
        UiMediaGridSkeletonHorizontalPreview()
    }
}
