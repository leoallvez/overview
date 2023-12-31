package br.dev.singular.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.dev.singular.overview.ui.navigation.onNavigateToMediaDetails
import br.dev.singular.overview.ui.navigation.onNavigateToStreamingExplore

open class BasicNavigate(
    private val navigation: NavController,
    private val backstack: Boolean = false
) {

    fun toMediaDetails(apiId: Long, mediaType: String?, backstack: Boolean = false) =
        onNavigateToMediaDetails(navigation, backstack)
            .invoke(apiId, mediaType)

    fun popBackStack() {
        if (backstack) {
            onNavigateToStreamingExplore(navigation).invoke()
        } else {
            navigation.popBackStack()
        }
    }
}
