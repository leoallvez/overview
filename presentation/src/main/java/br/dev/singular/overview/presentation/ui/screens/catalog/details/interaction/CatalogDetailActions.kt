package br.dev.singular.overview.presentation.ui.screens.catalog.details.interaction

import androidx.compose.runtime.Immutable
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.model.ScrollUiState
import br.dev.singular.overview.presentation.tagging.TagMediaManager
import br.dev.singular.overview.presentation.ui.navigation.Destination
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper

@Immutable
data class CatalogDetailActions(
    private val navigation: INavigationWrapper? = null,
    private val handleIntent: (intent: CatalogDetailIntent) -> Unit = {},
) {
    val tagPath: String = "/catalog-details"

    fun onSetType(type: MediaUiType) {
        TagMediaManager.logTypeClick(tagPath, type = type)
        handleIntent(CatalogDetailIntent.SelectType(type))
    }

    fun onSetScrollState(state: ScrollUiState) =
        handleIntent(CatalogDetailIntent.ChangeScrollState(state))

    fun navigateToMediaDetails(media: MediaUiModel) {
        TagMediaManager.logMediaClick(tagPath, media.id)
        navigation?.toMediaDetails(media)
    }

    fun navigateToCatalog() {
        navigation?.navigate(route = Destination.ChangeCatalog.route)
    }

    fun navigateToGenre() {
        navigation?.navigate(route = Destination.SelectGenre.route)
    }
}

