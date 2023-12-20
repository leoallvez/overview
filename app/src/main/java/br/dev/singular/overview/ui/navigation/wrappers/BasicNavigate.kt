package br.dev.singular.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.dev.singular.overview.ui.navigation.onNavigateSelectStreaming
import br.dev.singular.overview.ui.navigation.onNavigateToMediaDetails

open class BasicNavigate(
    private val navigation: NavController,
    private val backstack: Boolean = false
) {

    fun toMediaDetails(apiId: Long, mediaType: String?, backstack: Boolean = false) =
        onNavigateToMediaDetails(navigation, backstack)
            .invoke(apiId, mediaType)

    fun popBackStack() {
        if (backstack) {
            onNavigateSelectStreaming(navigation).invoke()
        } else {
            navigation.popBackStack()
        }
    }
}
