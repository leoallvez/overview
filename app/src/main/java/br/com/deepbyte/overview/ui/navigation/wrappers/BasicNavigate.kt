package br.com.deepbyte.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.com.deepbyte.overview.ui.navigation.onNavigateToHome
import br.com.deepbyte.overview.ui.navigation.onNavigateToMediaDetails

open class BasicNavigate(
    private val navigation: NavController,
    private val backToHome: Boolean = false
) {
    private fun toHome() = onNavigateToHome(navigation).invoke()

    fun toMediaDetails(apiId: Long, mediaType: String?, backToHome: Boolean = false) =
        onNavigateToMediaDetails(navigation, backToHome)
            .invoke(apiId, mediaType)

    fun popBackStack() {
        if (backToHome) {
            toHome()
        } else {
            navigation.popBackStack()
        }
    }
}
