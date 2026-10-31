package br.dev.singular.overview.presentation.ui.screens.seasons

import br.dev.singular.overview.presentation.ui.components.UiScreenSnapshotTest
import org.junit.Test

class SeasonsSnapshotTest : UiScreenSnapshotTest(snapshotPackage = "screens/seasons") {

    @Test
    fun default() = snapshot {
        SeasonsScreenPreview()
    }

    @Test
    fun skeleton() = snapshot {
        SeasonsSkeletonScreenPreview()
    }

    @Test
    fun loadingEpisodes() = snapshot {
        SeasonsScreenLoadingSeasonsPreview()
    }

    @Test
    fun withSnackbar() = gif(duration = 2_000L) {
        SeasonsScreenPreview()
    }
}
