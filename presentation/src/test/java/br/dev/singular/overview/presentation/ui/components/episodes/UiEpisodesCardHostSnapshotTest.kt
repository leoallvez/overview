package br.dev.singular.overview.presentation.ui.components.episodes

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

private const val STATES_LOOP_DURATION_MILLIS = 10_000L

class UiEpisodesCardHostSnapshotTest :
    UiSnapshotTest(snapshotPackage = "components/episodes") {

    @Test
    fun animation() = gif(duration = STATES_LOOP_DURATION_MILLIS) {
        UiEpisodesCardHostPreview()
    }
}
