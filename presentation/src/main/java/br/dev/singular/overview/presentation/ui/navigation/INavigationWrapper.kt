package br.dev.singular.overview.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavOptionsBuilder
import br.dev.singular.overview.presentation.model.MediaUiModel

interface INavigationWrapper {
    val startDestinationId: Int
    fun navigate(route: String, builder: NavOptionsBuilder.() -> Unit = {})
    fun toHome()
    fun popBackStack()
    fun toMediaDetails(media: MediaUiModel)

    val activeRoute: String?

    @Composable
    fun getCurrentRoute(): String?
}
