package br.com.deepbyte.overview.ui.navigation.events

import androidx.navigation.NavController
import br.com.deepbyte.overview.ui.navigation.onNavigateToHome
import br.com.deepbyte.overview.ui.navigation.onNavigateToMediaDetails
open class BasicsMediaEvents(private val navigation: NavController) {

    fun onNavigateToHome() = onNavigateToHome(navigation).invoke()

    fun onNavigateToMediaDetails(apiId: Long, mediaType: String?) =
        onNavigateToMediaDetails(navigation).invoke(apiId, mediaType)

    fun navigateToHome() = onNavigateToHome(navigation).invoke()
}
