package io.github.leoallvez.take.ui

sealed class Screen(val route: String, val name: String, val paramName: String = "") {
    object Splash: Screen(route = "splash_screen", name = "SplashScreen")

    object Home: Screen(route = "home_screen", name = "HomeScreen")

    object MediaDetails: Screen(
        route = "media_detail/{media_id}",
        name = "MediaDetail",
        paramName = "media_id"
    ) {
        fun editRoute(id: Long) = "media_detail/$id"
    }
}