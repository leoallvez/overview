package br.dev.singular.overview.presentation.ui.screens.search

import br.dev.singular.overview.presentation.ui.components.UiScreenSnapshotTest
import org.junit.Test

class SearchSnapshotTest : UiScreenSnapshotTest(snapshotPackage = "screens/search") {

    @Test
    fun suggestions() = snapshot {
        SuggestionScreenPreview()
    }

    @Test
    fun loading() = snapshot {
        SuggestionScreenLoadingPreview()
    }

    @Test
    fun error() = snapshot {
        SuggestionScreenErrorPreview()
    }

    @Test
    fun searchResults() = snapshot {
        SearchScreenPreview()
    }
}
