package br.dev.singular.overview.presentation.ui.components.media.item

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import br.dev.singular.overview.presentation.ui.components.media.UiMediaItemPreview
import br.dev.singular.overview.presentation.ui.components.media.UiMediaItemSkeletonPreview
import br.dev.singular.overview.presentation.ui.components.media.UiMediaWithLongTitlePreview
import org.junit.Test

class UiMediaItemSnapshotTest : UiSnapshotTest(snapshotPackage = "components/media/item") {

    @Test
    fun default() = snapshot {
        UiMediaItemPreview()
    }

    @Test
    fun longTitle() = snapshot {
        UiMediaWithLongTitlePreview()
    }

    @Test
    fun skeleton() = snapshot {
        UiMediaItemSkeletonPreview()
    }
}
