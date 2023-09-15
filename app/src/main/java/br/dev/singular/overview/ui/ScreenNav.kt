package br.dev.singular.overview.ui

import android.net.Uri

sealed class ScreenNav(val route: String, val name: String) {
    object Splash : ScreenNav(route = "splash_screen", name = "SplashScreen")

    object Home : ScreenNav(route = "home_screen", name = "HomeScreen")

    object MediaDetails : ScreenNav(
        route = "media_details/{$ID_PARAM}/{$TYPE_PARAM}/{$BACK_TO_HOME_PARAM}",
        name = "MediaDetailScreen"
    ) {
        fun editRoute(id: Long, type: String?, backToHome: Boolean = false) =
            "media_details/$id/$type/$backToHome"
    }

    object CastDetails : ScreenNav(
        route = "cast_details/{$ID_PARAM}",
        name = "CastDetailsScreen"
    ) {
        fun editRoute(id: Long) = "cast_details/$id"
    }

    object Search : ScreenNav(
        route = "search",
        name = "SearchScreen"
    )

    object StreamingExplore : ScreenNav(
        route = "streaming_explore/{$JSON_PARAM}",
        name = "StreamingExploreScreen"
    ) {
        fun editRoute(json: String) = "streaming_explore/${Uri.encode(json)}"
    }

    object StreamingExploreEdit : ScreenNav(
        route = "streaming_explore_edit",
        name = "StreamingExploreEditScreen"
    )

    companion object {
        const val ID_PARAM = "id"
        const val TYPE_PARAM = "type"
        const val JSON_PARAM = "json"
        const val BACK_TO_HOME_PARAM = "back_to_home"
    }
}
