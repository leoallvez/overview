package br.dev.singular.overview.presentation.ui.screens.media.movie

import br.dev.singular.overview.presentation.ui.components.UiScreenSnapshotTest
import org.junit.Test

class MovieDetailsSnapshotTest : UiScreenSnapshotTest(snapshotPackage = "screens/media/movie") {

    @Test
    fun default() = snapshot {
        MovieDetailsScreenPreview()
    }

    @Test
    fun loading() = snapshot {
        MovieDetailsScreenLoadingPreview()
    }

    @Test
    fun error() = snapshot {
        MovieDetailsScreenErrorPreview()
    }
}
