package io.github.leoallvez.take.ui

sealed class ScreenNav(val route: String, val name: String) {
    object Splash : ScreenNav(route = "splash_screen", name = "SplashScreen")

    object Home : ScreenNav(route = "home_screen", name = "HomeScreen")

    object MediaDetails : ScreenNav(
        route = "media_details/{$ID_PARAM}/{$TYPE_PARAM}",
        name = "MediaDetail"
    ) {
        fun editRoute(id: Long, type: String?) = "media_details/$id/$type"
    }

    object CastDetails : ScreenNav(
        route = "cast_details/{$ID_PARAM}",
        name = "CastDetails"
    ) {
        fun editRoute(id: Long) = "cast_details/$id"
    }

    object ProviderDiscover : ScreenNav(
        route = "provider_discover/{$JSON_PARAM}",
        name = "Discover"
    ) {
        fun editRoute(jsonParam: String) = "provider_discover/$jsonParam"
    }

    object GenreDiscover : ScreenNav(
        route = "genre_discover/{$JSON_PARAM}",
        name = "Discover"
    ) {
        fun editRoute(jsonParam: String) = "genre_discover/$jsonParam"
    }

    companion object {
        const val ID_PARAM = "id"
        const val TYPE_PARAM = "type"
        const val JSON_PARAM = "Json"
    }
}
