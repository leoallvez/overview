package br.com.deepbyte.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.com.deepbyte.overview.ui.ScreenNav
import br.com.deepbyte.overview.ui.navigation.onNavigateToMediaDetails

class HomeNavigate(private val navigation: NavController) {

    fun toMediaDetails(apiId: Long, mediaType: String?) =
        onNavigateToMediaDetails(navigation).invoke(apiId, mediaType)

    fun toSearch() = navigation.navigate(route = ScreenNav.Search.route)

    fun toStreamingExplore(json: String) =
        navigation.navigate(route = ScreenNav.StreamingExplore.editRoute(json))

    fun toStreamingExploreEdit() =
        navigation.navigate(route = ScreenNav.StreamingExploreEdit.route)
}
