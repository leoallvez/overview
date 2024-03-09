package br.dev.singular.overview.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import br.dev.singular.overview.ui.ScreenNav

class NavArg {
    companion object {
        val ID = navArgument(name = ScreenNav.ID_PARAM) { type = NavType.LongType }
        val TYPE = navArgument(name = ScreenNav.TYPE_PARAM) { type = NavType.StringType }
        val BACKSTACK = navArgument(name = ScreenNav.BACKSTACK_PARAM) { type = NavType.BoolType }
    }
}
