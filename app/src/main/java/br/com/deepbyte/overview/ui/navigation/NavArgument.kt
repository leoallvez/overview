package br.com.deepbyte.overview.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import br.com.deepbyte.overview.ui.ScreenNav

class NavArgument {
    companion object {
        val ID = navArgument(name = ScreenNav.ID_PARAM) { type = NavType.LongType }
        val JSON = navArgument(name = ScreenNav.JSON_PARAM) { type = NavType.StringType }
        val TYPE = navArgument(name = ScreenNav.TYPE_PARAM) { type = NavType.StringType }
    }
}
