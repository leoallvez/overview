package br.dev.singular.overview.presentation.ui.components.video.item

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import br.dev.singular.overview.presentation.ui.components.video.UiVideoItemPreview
import br.dev.singular.overview.presentation.ui.components.video.UiVideoItemSkeletonPreview
import org.junit.Test

class UiVideoItemSnapshotTest : UiSnapshotTest(snapshotPackage = "components/video/item") {

    @Test
    fun default() = snapshot {
        UiVideoItemPreview()
    }

    @Test
    fun skeleton() = snapshot {
        UiVideoItemSkeletonPreview()
    }
}
