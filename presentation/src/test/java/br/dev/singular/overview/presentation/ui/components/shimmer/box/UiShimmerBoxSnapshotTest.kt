package br.dev.singular.overview.presentation.ui.components.shimmer.box

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerBoxPreview
import org.junit.Test

class UiShimmerBoxSnapshotTest : UiSnapshotTest(snapshotPackage = "components/shimmer/box") {

    @Test
    fun default() = snapshot {
        UiShimmerBoxPreview()
    }
}
