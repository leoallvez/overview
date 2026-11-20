package br.dev.singular.overview.navigation.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.dev.singular.overview.presentation.ui.navigation.Destination
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper
import br.dev.singular.overview.presentation.ui.screens.video.YouTubePlayerFullscreen
import br.dev.singular.overview.presentation.ui.screens.video.interaction.YouTubePlayerActions

fun NavGraphBuilder.youTubePlayerRoute(
    navigation: INavigationWrapper,
    setEdgeToEdge: (Boolean) -> Unit
) {
    composable(
        route = Destination.YouTubePlayer.route,
        arguments = listOf(
            navArgument(name = Destination.VIDEO_KEY_PARAM) { type = NavType.StringType }
        )
    ) { backStackEntry ->
        YouTubePlayerFullscreen(
            setEdgeToEdge = setEdgeToEdge,
            videoKey = backStackEntry.arguments?.getString(Destination.VIDEO_KEY_PARAM) ?: "",
            actions = YouTubePlayerActions(navigation = navigation)
        )
    }
}
