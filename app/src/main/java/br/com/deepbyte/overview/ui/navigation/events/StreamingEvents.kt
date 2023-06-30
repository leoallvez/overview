package br.com.deepbyte.overview.ui.navigation.events

import androidx.navigation.NavController
import br.com.deepbyte.overview.ui.ScreenNav

class StreamingEvents(
    private val navigation: NavController
) : BasicsMediaEvents(navigation, backToHome = false) {

    fun onNavigateToSearch() = navigation.navigate(route = ScreenNav.Search.route)
}
