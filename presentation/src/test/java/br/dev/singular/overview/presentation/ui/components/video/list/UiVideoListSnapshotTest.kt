package br.dev.singular.overview.presentation.ui.components.video.list

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import br.dev.singular.overview.presentation.ui.components.video.UiVideoListEmptyPreview
import br.dev.singular.overview.presentation.ui.components.video.UiVideoListPreview
import br.dev.singular.overview.presentation.ui.components.video.UiVideoListSkeletonPreview
import org.junit.Test

class UiVideoListSnapshotTest : UiSnapshotTest(snapshotPackage = "components/video/list") {

    @Test
    fun default() = snapshot {
        UiVideoListPreview()
    }

    @Test
    fun empty() = snapshot {
        UiVideoListEmptyPreview()
    }

    @Test
    fun skeleton() = snapshot {
        UiVideoListSkeletonPreview()
    }
}
