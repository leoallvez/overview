package br.com.deepbyte.overview.ui.navigation.events

import androidx.navigation.NavController
import br.com.deepbyte.overview.ui.navigation.onNavigateToHome
import br.com.deepbyte.overview.ui.navigation.onNavigateToMediaDetails

open class BasicsMediaEvents(
    private val navigation: NavController,
    private val backToHome: Boolean = false,
) {
    fun onNavigateToHome() = onNavigateToHome(navigation).invoke()

    fun onNavigateToMediaDetails(apiId: Long, mediaType: String?, backToHome: Boolean = false) =
        onNavigateToMediaDetails(navigation, backToHome).invoke(apiId, mediaType)

    fun onPopBackStack() {
        if (backToHome) {
            onNavigateToHome()
        } else {
            navigation.popBackStack()
        }
    }
}
