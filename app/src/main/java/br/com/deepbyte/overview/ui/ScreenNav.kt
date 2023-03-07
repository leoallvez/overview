package br.com.deepbyte.overview.ui

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

    object ProviderDiscover : ScreenNav(
        route = "provider_discover/{$JSON_PARAM}",
        name = "ProviderDiscoverScreen"
    ) {
        fun editRoute(jsonParam: String) = "provider_discover/$jsonParam"
    }

    object GenreDiscover : ScreenNav(
        route = "genre_discover/{$JSON_PARAM}",
        name = "GenreDiscoverScreen"
    ) {
        fun editRoute(jsonParam: String) = "genre_discover/$jsonParam"
    }

    object Search : ScreenNav(
        route = "search",
        name = "SearchScreen"
    )

    object StreamingExplore : ScreenNav(
        route = "streaming_overview/{$ID_PARAM}",
        name = "StreamingOverviewScreen"
    ) {
        fun editRoute(id: Long) = "streaming_overview/$id"
    }

    object StreamingOverviewEdit : ScreenNav(
        route = "streaming_overview_edit",
        name = "StreamingOverviewEditScreen"
    )

    companion object {
        const val ID_PARAM = "id"
        const val TYPE_PARAM = "type"
        const val JSON_PARAM = "Json"
        const val BACK_TO_HOME_PARAM = "back_to_home"
    }
}
