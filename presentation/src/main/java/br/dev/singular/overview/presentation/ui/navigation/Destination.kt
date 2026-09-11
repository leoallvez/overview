package br.dev.singular.overview.presentation.ui.navigation

sealed class Destination(val route: String) {

    object Splash : Destination(route = "splash-screen")

    object Search : Destination(route = "search")

    object Favorites : Destination(route = "favorites")

    object MediaDetails : Destination(
        route = "media-details/{$ID_PARAM}/{$TYPE_PARAM}/{$BACKSTACK_PARAM}"
    ) {
        fun editRoute(id: Long, type: String?, backstack: Boolean = false) =
            "media-details/$id/$type/$backstack"
    }

    object YouTubePlayer : Destination(
        route = "youtube-player/{$VIDEO_KEY_PARAM}"
    ) {
        fun editRoute(videoKey: String) = "youtube-player/$videoKey"
    }

    object PersonDetails : Destination(
        route = "person-details/{$ID_PARAM}"
    ) {
        fun editRoute(id: Long) = "person-details/$id"
    }

    object ChangeCatalog : Destination(
        route = "change-catalog",
    )

    object SelectCatalog : Destination(
        route = "select-catalog",
    )

    object SelectGenre : Destination(
        route = "select-genre",
    )

    object CatalogDetails : Destination(
        route = "catalog-details",
    )

    companion object {
        const val ID_PARAM = "id"
        const val TYPE_PARAM = "type"
        const val VIDEO_KEY_PARAM = "videoKey"
        const val BACKSTACK_PARAM = "backstack"
    }
}
