package br.dev.singular.overview.presentation.ui.screens.search.interaction

import androidx.compose.runtime.Immutable
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.TagMediaManager
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper

@Immutable
data class SearchActions(
    val handleIntent: (SearchIntent) -> Unit = {},
    val onSetType: (MediaUiType) -> Unit = {},
    private val navigation: INavigationWrapper? = null,
) {
    val tagPath: SearchTagPath by lazy { SearchTagPath() }

    fun onLoadSuggestions() = handleIntent(SearchIntent.LoadSuggestions)

    fun onSearch(query: String) {
        TagManager.logInteraction(customPath = tagPath.search, detail = "search-field")
        handleIntent(SearchIntent.Search(query))
    }

    fun onSelectType(type: MediaUiType) {
        TagMediaManager.logTypeClick(path = tagPath.search, type)
        handleIntent(SearchIntent.SetType(type))
        onSetType(type)
    }

    fun onClear() {
        TagManager.logClick(customPath = tagPath.search, detail = "clean-search-field")
    }

    fun onToMediaDetails(media: MediaUiModel, isSuggestion: Boolean = false) {
        TagMediaManager.logMediaClick(
            path = if (isSuggestion) tagPath.suggestion else tagPath.search,
            id = media.id
        )
        navigation?.toMediaDetails(media)
    }
}

@Immutable
class SearchTagPath(
    val search: String = "/search",
    val suggestion: String = "/search:suggestions"
)
