package br.com.deepbyte.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.com.deepbyte.overview.ui.navigation.toHome
import br.com.deepbyte.overview.ui.navigation.toMediaDetails

open class BasicNavigation(
    private val navigation: NavController,
    private val backToHome: Boolean = false
) {
    private fun toHome() = toHome(navigation).invoke()

    fun toMediaDetails(apiId: Long, mediaType: String?, backToHome: Boolean = false) =
        toMediaDetails(navigation, backToHome)
            .invoke(apiId, mediaType)

    fun popBackStack() {
        if (backToHome) {
            toHome()
        } else {
            navigation.popBackStack()
        }
    }
}
