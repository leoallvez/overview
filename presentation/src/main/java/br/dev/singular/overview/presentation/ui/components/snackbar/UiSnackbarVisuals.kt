package br.dev.singular.overview.presentation.ui.components.snackbar

import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.snackbar.style.UiSnackbarStyle

sealed interface UiSnackbarVisuals {
    val title: String
    val message: String
    val style: UiSnackbarStyle
    val durationMillis: Long
    val icon: UiIconSource

    data class Close(
        override val title: String,
        override val message: String,
        override val style: UiSnackbarStyle = UiSnackbarStyle.Success,
        override val durationMillis: Long = 4000L,
        override val icon: UiIconSource = style.defaultIcon,
        val onClose: () -> Unit = {}
    ) : UiSnackbarVisuals

    data class Action(
        override val title: String,
        override val message: String,
        val actionText: String,
        override val style: UiSnackbarStyle = UiSnackbarStyle.Success,
        override val durationMillis: Long = 4000L,
        override val icon: UiIconSource = style.defaultIcon,
        val onAction: () -> Unit
    ) : UiSnackbarVisuals
}
