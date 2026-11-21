package br.dev.singular.overview.presentation.ui.components.episodes.item

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiEpisodeItemSnapshotTest : UiSnapshotTest(snapshotPackage = "components/episodes/item") {

    @Test
    fun watched() = snapshot {
        UiEpisodeItemWatchedPreview()
    }

    @Test
    fun unwatched() = snapshot {
        UiEpisodeItemUnwatchedPreview()
    }

    @Test
    fun skeleton() = snapshot {
        UiEpisodeItemSkeletonPreview()
    }
}
