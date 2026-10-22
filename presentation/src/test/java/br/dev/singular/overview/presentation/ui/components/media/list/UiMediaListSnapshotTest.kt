package br.dev.singular.overview.presentation.ui.components.media.list

import br.dev.singular.overview.presentation.ui.components.UiScreenSnapshotTest
import br.dev.singular.overview.presentation.ui.components.media.UiMediaListEmptyPreview
import br.dev.singular.overview.presentation.ui.components.media.UiMediaListSkeletonPreview
import br.dev.singular.overview.presentation.ui.components.media.UiMediaListWitchContentPaddingPreview
import br.dev.singular.overview.presentation.ui.components.media.UiMediaListWithBackgroundPreview
import br.dev.singular.overview.presentation.ui.components.media.UiMediaPreview
import org.junit.Test

class UiMediaListSnapshotTest : UiScreenSnapshotTest(snapshotPackage = "components/media/list") {

    @Test
    fun default() = snapshot {
        UiMediaPreview()
    }

    @Test
    fun withContentPadding() = snapshot {
        UiMediaListWitchContentPaddingPreview()
    }

    @Test
    fun withBackground() = snapshot {
        UiMediaListWithBackgroundPreview()
    }

    @Test
    fun empty() = snapshot {
        UiMediaListEmptyPreview()
    }

    @Test
    fun skeleton() = snapshot {
        UiMediaListSkeletonPreview()
    }
}
