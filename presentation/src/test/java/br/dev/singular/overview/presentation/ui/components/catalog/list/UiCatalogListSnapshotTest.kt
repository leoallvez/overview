package br.dev.singular.overview.presentation.ui.components.catalog.list

import br.dev.singular.overview.presentation.ui.components.UiScreenSnapshotTest
import br.dev.singular.overview.presentation.ui.components.catalog.UiCatalogListEmptyNotReleasedPreview
import br.dev.singular.overview.presentation.ui.components.catalog.UiCatalogListEmptyReleasedPreview
import br.dev.singular.overview.presentation.ui.components.catalog.UiCatalogListPreview
import br.dev.singular.overview.presentation.ui.components.catalog.UiCatalogListWithContentPaddingPreview
import org.junit.Test

class UiCatalogListSnapshotTest : UiScreenSnapshotTest(snapshotPackage = "components/catalog/list") {

    @Test
    fun default() = snapshot {
        UiCatalogListPreview()
    }

    @Test
    fun withContentPadding() = snapshot {
        UiCatalogListWithContentPaddingPreview()
    }

    @Test
    fun emptyReleased() = snapshot {
        UiCatalogListEmptyReleasedPreview()
    }

    @Test
    fun emptyNotReleased() = snapshot {
        UiCatalogListEmptyNotReleasedPreview()
    }
}
