package br.dev.singular.overview.presentation.ui.screens.media.tvshow

import br.dev.singular.overview.presentation.ui.components.UiScreenSnapshotTest
import org.junit.Test

class TvShowDetailsSnapshotTest : UiScreenSnapshotTest(snapshotPackage = "screens/media/tvshow") {

    @Test
    fun default() = snapshot {
        TvShowDetailsScreenPreview()
    }

    @Test
    fun loading() = snapshot {
        TvShowDetailsScreenLoadingPreview()
    }

    @Test
    fun error() = snapshot {
        TvShowDetailsScreenErrorPreview()
    }
}
