package br.dev.singular.overview.presentation.ui.components.appbar

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import br.dev.singular.overview.presentation.ui.components.navigation.UiToolbarDefaultPreview
import br.dev.singular.overview.presentation.ui.components.navigation.UiToolbarLeadingPreview
import br.dev.singular.overview.presentation.ui.components.navigation.UiToolbarTrailingContentPreview
import br.dev.singular.overview.presentation.ui.components.navigation.UiToolbarTrailingPreview
import org.junit.Test

class UiTopAppBarSnapshotTest : UiSnapshotTest(snapshotPackage = "components/appbar") {

    @Test
    fun default() = snapshot {
        UiToolbarDefaultPreview()
    }

    @Test
    fun trailing() = snapshot {
        UiToolbarTrailingPreview()
    }

    @Test
    fun leading() = snapshot {
        UiToolbarLeadingPreview()
    }

    @Test
    fun trailingContent() = snapshot {
        UiToolbarTrailingContentPreview()
    }
}
