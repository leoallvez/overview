package br.dev.singular.overview.ui

sealed class ScreenNav(val route: String, val name: String) {
    object Splash : ScreenNav(route = "splash_screen", name = "SplashScreen")

    object MediaDetails : ScreenNav(
        route = "media_details/{$ID_PARAM}/{$TYPE_PARAM}/{$BACKSTACK_PARAM}",
        name = "MediaDetailScreen"
    ) {
        fun editRoute(id: Long, type: String?, backstack: Boolean = false) =
            "media_details/$id/$type/$backstack"
    }

    object PersonDetails : ScreenNav(
        route = "person_details/{$ID_PARAM}",
        name = "PersonDetailsScreen"
    ) {
        fun editRoute(id: Long) = "person_details/$id"
    }

    object Search : ScreenNav(
        route = "search",
        name = "SearchScreen"
    )

    object Liked : ScreenNav(
        route = "liked",
        name = "LikedScreen"
    )

    object ExploreStreaming : ScreenNav(
        route = "explore_streaming",
        name = "ExploreStreamingScreen"
    )

    object SelectStreaming : ScreenNav(
        route = "select_streaming",
        name = "SelectStreamingScreen"
    )

    companion object {
        const val ID_PARAM = "id"
        const val TYPE_PARAM = "type"
        const val BACKSTACK_PARAM = "backstack"
    }
}
