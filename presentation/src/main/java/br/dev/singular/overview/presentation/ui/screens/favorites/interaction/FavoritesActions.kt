package br.dev.singular.overview.presentation.ui.screens.favorites.interaction

import androidx.compose.runtime.Immutable
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.TagMediaManager
import br.dev.singular.overview.presentation.ui.navigation.Destination
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper

@Immutable
data class FavoritesActions(
    private val navigation: INavigationWrapper? = null,
    val onSetType: (MediaUiType) -> Unit = {},
    val onSetQuery: (String) -> Unit = {},
) {
    val tagPath: String = "/favorites"

    fun onSelectType(type: MediaUiType) {
        TagMediaManager.logTypeClick(tagPath, type)
        onSetType(type)
    }

    fun onSearch(query: String) {
        TagManager.logInteraction(customPath = tagPath, detail = "search-field")
        onSetQuery(query)
    }

    fun onClear() {
        TagManager.logClick(customPath = tagPath, detail = "clean-search-field")
    }

    fun onToMediaDetails(media: MediaUiModel) {
        TagMediaManager.logMediaClick(tagPath, media.id)
        navigation?.toMediaDetails(media)
    }

    fun onToProfile() {
        TagManager.logClick(customPath = tagPath, detail = "profile")
        navigation?.navigate(Destination.Profile.route)
    }
}
