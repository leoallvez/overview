package br.dev.singular.overview.presentation.ui.screens.genre.interaction

import androidx.compose.runtime.Immutable
import br.dev.singular.overview.presentation.model.GenreUiModel
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper

@Immutable
data class GenreSelectionActions(
    private val navigation: INavigationWrapper? = null,
    val handleIntent: (GenreSelectionIntent) -> Unit = {}
) {
    val tagPath: String = "/select-genre"

    fun onLoad() = handleIntent(GenreSelectionIntent.Load)

    fun onSelect(genre: GenreUiModel?) {
        TagManager.logClick(customPath = tagPath, detail = "on-select")
        handleIntent(GenreSelectionIntent.Select(genre))
        navigation?.toHome()
    }

    fun onBack() {
        TagManager.logClick(customPath = tagPath, detail = "close")
        navigation?.popBackStack()
    }
}
