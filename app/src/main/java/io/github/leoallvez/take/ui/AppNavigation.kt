package io.github.leoallvez.take.ui

sealed class Screen(val route: String, val name: String) {
    object Splash : Screen(route = "splash_screen", name = "SplashScreen")

    object Home : Screen(route = "home_screen", name = "HomeScreen")

    object MediaDetails : Screen(
        route = "media_details/{$ID_PARAM}/{$TYPE_PARAM}",
        name = "MediaDetail"
    ) {
        fun editRoute(id: Long, type: String?) = "media_details/$id/$type"
    }

    object CastDetails : Screen(
        route = "cast_details/{$ID_PARAM}",
        name = "CastDetails"
    ) {
        fun editRoute(id: Long) = "cast_details/$id"
    }

    object Discover : Screen(
        route = "discover/{$ID_PARAM}",
        name = "Discover"
    ) {
        fun editRoute(id: Long) = "discover/$id"
    }

    companion object {
        const val ID_PARAM = "id"
        const val TYPE_PARAM = "type"
    }
}