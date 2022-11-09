package io.github.leoallvez.take.ui.navigation

import androidx.navigation.NavController
import io.github.leoallvez.take.ui.ScreenNav

class MediaDetailsScreenEvents(private val navigation: NavController) {

    fun onNavigateToHome() = navigation.navigate(ScreenNav.Home.route)

    fun onNavigateToProviderDiscover(json: String) = navigation
        .navigate(ScreenNav.ProviderDiscover.editRoute(json))

    fun onNavigateToGenreDiscover(json: String) = navigation
        .navigate(ScreenNav.GenreDiscover.editRoute(json))

    fun onNavigateToCastDetails(apiId: Long) = navigation
        .navigate(ScreenNav.CastDetails.editRoute(apiId))

    fun onNavigateToMediaDetails(apiId: Long, mediaType: String?) {
        navigation.navigate(
            ScreenNav.MediaDetails.editRoute(id = apiId, type = mediaType)
        )
    }
}
