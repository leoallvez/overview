package br.dev.singular.overview.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import br.dev.singular.overview.ui.ScreenNav

class NavArgument {
    companion object {
        val ID = navArgument(name = ScreenNav.ID_PARAM) { type = NavType.LongType }
        val TYPE = navArgument(name = ScreenNav.TYPE_PARAM) { type = NavType.StringType }
        val BACK_TO_HOME = navArgument(
            name = ScreenNav.BACK_TO_HOME_PARAM
        ) { type = NavType.BoolType }
    }
}
