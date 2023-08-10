package br.com.deepbyte.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.com.deepbyte.overview.ui.ScreenNav

class MediaDetailsNavigation(
    private val navigation: NavController,
    backToHome: Boolean = false
) : BasicNavigation(navigation, backToHome) {

    fun toStreamingExplore(json: String) =
        navigation.navigate(ScreenNav.StreamingExplore.editRoute(json))

    fun toCastDetails(apiId: Long) =
        navigation.navigate(ScreenNav.CastDetails.editRoute(apiId))
}
