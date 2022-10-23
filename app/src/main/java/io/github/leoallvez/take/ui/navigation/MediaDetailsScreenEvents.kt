package io.github.leoallvez.take.ui.navigation

import androidx.navigation.NavController
import io.github.leoallvez.take.ui.ScreenNav

class MediaDetailsScreenEvents(private val navigation: NavController) {

    fun onNavigateToHome() = navigation.navigate(ScreenNav.Home.route)

    fun onNavigateToDiscover(apiId: Long) = navigation
        .navigate(ScreenNav.Discover.editRoute(apiId))

    fun onNavigateToCastDetails(apiId: Long) = navigation
        .navigate(ScreenNav.CastDetails.editRoute(apiId))

    fun onNavigateToMediaDetails(apiId: Long, mediaType: String?) {
        navigation.navigate(
            ScreenNav.MediaDetails.editRoute(id = apiId, type = mediaType)
        )
    }
}
