package br.dev.singular.overview.presentation.ui.components.prediction

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiAiPredictionCardSnapshotTest : UiSnapshotTest(snapshotPackage = "components/prediction") {

    @Test
    fun idle() = snapshot {
        UiAiPredictionCardIdlePreview()
    }

    @Test
    fun high() = snapshot {
        UiAiPredictionCardHighPreview()
    }

    @Test
    fun medium() = snapshot {
        UiAiPredictionCardMediumPreview()
    }

    @Test
    fun low() = snapshot {
        UiAiPredictionCardLowPreview()
    }

    @Test
    fun unavailable() = snapshot {
        UiAiPredictionCardUnavailablePreview()
    }

    @Test
    fun loginRequired() = snapshot {
        UiAiPredictionCardLoginRequiredPreview()
    }

    @Test
    fun error() = snapshot {
        UiAiPredictionCardErrorPreview()
    }

    @Test
    fun loading() = snapshot {
        UiAiPredictionCardLoadingPreview()
    }

    @Test
    fun skeleton() = snapshot {
        UiAiPredictionCardSkeletonPreview()
    }
}
