package br.dev.singular.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.dev.singular.overview.ui.navigation.onNavigateToExploreStreaming
import br.dev.singular.overview.ui.navigation.onNavigateToMediaDetails

open class BasicNavigate(
    private val nav: NavController,
    private val backstack: Boolean = false
) {

    fun toMediaDetails(apiId: Long, mediaType: String?, backstack: Boolean = false) =
        onNavigateToMediaDetails(nav, backstack)
            .invoke(apiId, mediaType)

    fun popBackStack() {
        if (backstack) {
            onNavigateToExploreStreaming(nav).invoke()
        } else {
            nav.popBackStack()
        }
    }
}
