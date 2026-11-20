package br.dev.singular.overview.presentation.ui.components.prediction

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

private const val STATES_LOOP_DURATION_MILLIS = 14_000L

class UiAiPredictionCardHostSnapshotTest :
    UiSnapshotTest(snapshotPackage = "components/prediction") {

    @Test
    fun animation() = gif(duration = STATES_LOOP_DURATION_MILLIS) {
        UiAiPredictionCardHostPreview()
    }
}
