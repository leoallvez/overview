package br.dev.singular.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.dev.singular.overview.ui.ScreenNav

class MediaDetailsNavigate(
    private val navigation: NavController,
    backToHome: Boolean = false
) : BasicNavigate(navigation, backToHome) {

    fun toStreamingExplore(json: String) =
        navigation.navigate(ScreenNav.StreamingExplore.editRoute(json))

    fun toCastDetails(apiId: Long) =
        navigation.navigate(ScreenNav.CastDetails.editRoute(apiId))
}
