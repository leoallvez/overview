package br.dev.singular.overview.presentation.ui.screens.media

import br.dev.singular.overview.presentation.ui.components.UiScreenSnapshotTest
import org.junit.Test

class MediaDetailsSkeletonSnapshotTest : UiScreenSnapshotTest(snapshotPackage = "screens/media") {

    @Test
    fun skeleton() = snapshot {
        MediaDetailsSkeletonScreenPreview()
    }
}
