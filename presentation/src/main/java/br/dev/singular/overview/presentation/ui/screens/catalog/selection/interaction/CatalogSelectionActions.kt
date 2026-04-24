package br.dev.singular.overview.presentation.ui.screens.catalog.selection.interaction

import androidx.compose.runtime.Immutable
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper

@Immutable
data class CatalogSelectionActions(
    val tagPath: String = "",
    private val navigation: INavigationWrapper? = null,
    val handleIntent: (CatalogSelectionIntent) -> Unit = {}
) {

    fun onLoad() = handleIntent(CatalogSelectionIntent.Load)

    fun onSelect(catalog: CatalogUiModel, clearGenre: Boolean = false) {
        TagManager.logClick(customPath = tagPath, detail = "on-select", id = catalog.id)
        handleIntent(CatalogSelectionIntent.Select(catalog, clearGenre))
        navigation?.toHome()
    }

    fun onDismissTooltip() {
        TagManager.logClick(customPath = tagPath, detail = "dismiss-tooltip")
        handleIntent(CatalogSelectionIntent.DismissTooltip)
    }

    fun onBack() {
        TagManager.logClick(customPath = tagPath, detail = "close")
        navigation?.popBackStack()
    }
}
