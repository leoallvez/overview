package br.dev.singular.overview.presentation.ui.components.episodes.chip

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiWatchedChipSnapshotTest : UiSnapshotTest(snapshotPackage = "components/episodes/watched_chip") {

    @Test
    fun watched() = snapshot {
        UiWatchedChipWatchedPreview()
    }

    @Test
    fun mark() = snapshot {
        UiWatchedChipMarkPreview()
    }

    @Test
    fun skeleton() = snapshot {
        UiWatchedChipSkeletonPreview()
    }
}
