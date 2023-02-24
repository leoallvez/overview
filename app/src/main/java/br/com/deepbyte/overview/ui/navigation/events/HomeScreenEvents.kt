package br.com.deepbyte.overview.ui.navigation.events

import androidx.navigation.NavController
import br.com.deepbyte.overview.ui.ScreenNav
import br.com.deepbyte.overview.ui.navigation.onNavigateToMediaDetails

class HomeScreenEvents(private val navigation: NavController) {

    fun onNavigateToMediaDetails(apiId: Long, mediaType: String?) =
        onNavigateToMediaDetails(navigation).invoke(apiId, mediaType)

    fun onNavigateToSearch() = navigation.navigate(route = ScreenNav.Search.route)
}
