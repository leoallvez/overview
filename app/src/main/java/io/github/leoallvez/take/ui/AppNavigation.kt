package io.github.leoallvez.take.ui

sealed class Screen(val route: String, val name: String) {
    object Splash : Screen(route = "splash_screen", name = "SplashScreen")

    object Home : Screen(route = "home_screen", name = "HomeScreen")

    object MediaDetails : Screen(
        route = "media_detail/{$ID_PARAM}/{$TYPE_PARAM}",
        name = "MediaDetail"
    ) {
        fun editRoute(id: Long, type: String?) = "media_detail/$id/$type"
    }

    companion object {
        const val ID_PARAM = "id"
        const val TYPE_PARAM = "type"
    }
}