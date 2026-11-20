package br.dev.singular.overview.presentation.ui.components.search

import br.dev.singular.overview.presentation.ui.components.UiSearchFieldEmptyPreview
import br.dev.singular.overview.presentation.ui.components.UiSearchFieldWithQueryPreview
import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import org.junit.Test

class UiSearchFieldSnapshotTest : UiSnapshotTest(snapshotPackage = "components/search") {

    @Test
    fun empty() = snapshot {
        UiSearchFieldEmptyPreview()
    }

    @Test
    fun withQuery() = snapshot {
        UiSearchFieldWithQueryPreview()
    }
}
