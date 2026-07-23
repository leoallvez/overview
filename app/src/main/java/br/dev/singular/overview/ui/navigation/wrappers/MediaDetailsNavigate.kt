package br.dev.singular.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.dev.singular.overview.presentation.ui.navigation.Destination

class MediaDetailsNavigate(private val nav: NavController) : BasicNavigate(nav) {
    fun toPersonDetails(apiId: Long) = nav.navigate(Destination.PersonDetails.editRoute(apiId))
    fun toYouTubePlayer(videoKey: String) =
        nav.navigate(Destination.YouTubePlayer.editRoute(videoKey))
}
