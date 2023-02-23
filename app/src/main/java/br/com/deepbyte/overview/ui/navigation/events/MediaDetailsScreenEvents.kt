package br.com.deepbyte.overview.ui.navigation.events

import androidx.navigation.NavController
import br.com.deepbyte.overview.ui.ScreenNav

class MediaDetailsScreenEvents(
    private val navigation: NavController,
    backToHome: Boolean = false
) : BasicsMediaEvents(navigation, backToHome) {

    fun onNavigateToProviderDiscover(json: String) =
        navigation.navigate(ScreenNav.ProviderDiscover.editRoute(json))

    fun onNavigateToGenreDiscover(json: String) =
        navigation.navigate(ScreenNav.GenreDiscover.editRoute(json))

    fun onNavigateToCastDetails(apiId: Long) =
        navigation.navigate(ScreenNav.CastDetails.editRoute(apiId))
}
