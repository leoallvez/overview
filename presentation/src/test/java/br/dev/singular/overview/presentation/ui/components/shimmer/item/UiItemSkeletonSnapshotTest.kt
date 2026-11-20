package br.dev.singular.overview.presentation.ui.components.shimmer.item

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import br.dev.singular.overview.presentation.ui.components.shimmer.UiItemSkeletonPreview
import org.junit.Test

class UiItemSkeletonSnapshotTest : UiSnapshotTest(snapshotPackage = "components/shimmer/item") {

    @Test
    fun default() = snapshot {
        UiItemSkeletonPreview()
    }
}
