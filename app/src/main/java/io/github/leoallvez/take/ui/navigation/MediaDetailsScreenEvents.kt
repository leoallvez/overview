package io.github.leoallvez.take.ui.navigation

import androidx.navigation.NavController
import io.github.leoallvez.take.ui.Screen

class MediaDetailsScreenEvents(private val navigation: NavController) {

    fun onNavigateToHome()
            = navigation.navigate(Screen.Home.route)

    fun onNavigateToDiscover(apiId: Long)
            = navigation.navigate(Screen.Discover.editRoute(apiId))

    fun onNavigateToCastDetails(apiId: Long)
            = navigation.navigate(Screen.CastDetails.editRoute(apiId))

    fun onNavigateToMediaDetails(apiId: Long, mediaType: String?) {
        navigation.navigate(
            Screen.MediaDetails.editRoute(id = apiId, type = mediaType)
        )
    }
}
