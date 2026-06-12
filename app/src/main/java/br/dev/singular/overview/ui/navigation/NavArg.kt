package br.dev.singular.overview.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import br.dev.singular.overview.presentation.ui.navigation.Destination

class NavArg {
    companion object {
        val ID = navArgument(name = Destination.ID_PARAM) { type = NavType.LongType }
        val TYPE = navArgument(name = Destination.TYPE_PARAM) { type = NavType.StringType }
        val BACKSTACK = navArgument(name = Destination.BACKSTACK_PARAM) { type = NavType.BoolType }
    }
}
