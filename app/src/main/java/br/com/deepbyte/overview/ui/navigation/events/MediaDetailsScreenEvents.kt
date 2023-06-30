package br.com.deepbyte.overview.ui.navigation.events

import androidx.navigation.NavController
import br.com.deepbyte.overview.ui.ScreenNav

class MediaDetailsScreenEvents(
    private val navigation: NavController,
    backToHome: Boolean = false
) : BasicsMediaEvents(navigation, backToHome) {

    fun onNavigateToStreamingExplore(json: String) =
        navigation.navigate(ScreenNav.StreamingExplore.editRoute(json))

    fun onNavigateToCastDetails(apiId: Long) =
        navigation.navigate(ScreenNav.CastDetails.editRoute(apiId))
}
