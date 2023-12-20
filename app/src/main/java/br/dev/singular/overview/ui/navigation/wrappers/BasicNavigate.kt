package br.dev.singular.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.dev.singular.overview.ui.navigation.onBackstack
import br.dev.singular.overview.ui.navigation.onNavigateToMediaDetails

open class BasicNavigate(
    private val navigation: NavController,
    private val backstack: Boolean = false
) {
    private fun backstack() = onBackstack(navigation).invoke()

    fun toMediaDetails(apiId: Long, mediaType: String?, backstack: Boolean = false) =
        onNavigateToMediaDetails(navigation, backstack)
            .invoke(apiId, mediaType)

    fun popBackStack() {
        if (backstack) {
            backstack()
        } else {
            navigation.popBackStack()
        }
    }
}
