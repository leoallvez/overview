package br.dev.singular.overview.presentation.ui.components.genre

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiGenreListSnapshotTest : UiSnapshotTest(snapshotPackage = "components/genre") {

    @Test
    fun default() = snapshot {
        UiGenreListPreview()
    }

    @Test
    fun withContentPadding() = snapshot {
        UiGenreListWithContentPaddingPreview()
    }

    @Test
    fun withBackground() = snapshot {
        UiGenreListWithBackgroundPreview()
    }

    @Test
    fun empty() = snapshot {
        UiGenreListEmptyPreview()
    }

    @Test
    fun skeleton() = snapshot {
        UiGenreListSkeletonPreview()
    }
}
