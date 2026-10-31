package br.dev.singular.overview.presentation.ui.components.episodes

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiEpisodesCardSnapshotTest : UiSnapshotTest(snapshotPackage = "components/episodes") {

    @Test
    fun default() = snapshot {
        UiEpisodesCardDefaultPreview()
    }

    @Test
    fun inProgress() = snapshot {
        UiEpisodesCardInProgressPreview()
    }

    @Test
    fun completed() = snapshot {
        UiEpisodesCardCompletedPreview()
    }

    @Test
    fun signedOut() = snapshot {
        UiEpisodesCardSignedOutPreview()
    }

    @Test
    fun notReleased() = snapshot {
        UiEpisodesCardNotReleasedPreview()
    }

    @Test
    fun loading() = snapshot {
        UiEpisodesCardLoadingPreview()
    }

    @Test
    fun skeleton() = snapshot {
        UiEpisodesCardSkeletonPreview()
    }
}
