package br.dev.singular.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.dev.singular.overview.ui.ScreenNav

class MediaDetailsNavigate(private val nav: NavController) : BasicNavigate(nav) {
    fun toPersonDetails(apiId: Long) = nav.navigate(ScreenNav.PersonDetails.editRoute(apiId))
    fun toYouTubePlayer(videoKey: String) = nav.navigate(ScreenNav.YouTubePlayer.editRoute(videoKey))
}
