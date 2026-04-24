package br.dev.singular.overview.presentation.ui.navigation

import br.dev.singular.overview.presentation.model.MediaUiModel

interface INavigationWrapper {
    fun navigate(route: String)
    fun toHome()
    fun popBackStack()
    fun toMediaDetails(media: MediaUiModel)
}
