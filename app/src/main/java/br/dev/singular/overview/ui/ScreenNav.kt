package br.dev.singular.overview.ui

sealed class ScreenNav(val route: String, val name: String) {
    object Splash : ScreenNav(route = "splash_screen", name = "SplashScreen")

    object Home : ScreenNav(
        route = "home",
        name = "HomeScreen"
    )

    object Search : ScreenNav(
        route = "search",
        name = "SearchScreen"
    )

    object Favorites : ScreenNav(
        route = "liked",
        name = "FavoritesScreen"
    )

    object MediaDetails : ScreenNav(
        route = "media_details/{$ID_PARAM}/{$TYPE_PARAM}/{$BACKSTACK_PARAM}",
        name = "MediaDetailScreen"
    ) {
        fun editRoute(id: Long, type: String?, backstack: Boolean = false) =
            "media_details/$id/$type/$backstack"
    }

    object YouTubePlayer : ScreenNav(
        route = "youtube_player/{$VIDEO_KEY_PARAM}",
        name = "YouTubePlayerFullscreen"
    ) {
        fun editRoute(videoKey: String) = "youtube_player/$videoKey"
    }

    object PersonDetails : ScreenNav(
        route = "person_details/{$ID_PARAM}",
        name = "PersonDetailsScreen"
    ) {
        fun editRoute(id: Long) = "person_details/$id"
    }

    object SelectStreaming : ScreenNav(
        route = "select_streaming",
        name = "SelectStreamingScreen"
    )

    companion object {
        const val ID_PARAM = "id"
        const val TYPE_PARAM = "type"
        const val VIDEO_KEY_PARAM = "videoKey"
        const val BACKSTACK_PARAM = "backstack"
    }
}
