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

    data class Persistent(
        override val title: String,
        override val message: String,
        override val style: UiSnackbarStyle = UiSnackbarStyle.Success,
        override val icon: UiIconSource = style.defaultIcon
    ) : UiSnackbarVisuals {
        override val durationMillis: Long = INDEFINITE_DURATION_MILLIS
    }

    companion object {
        const val INDEFINITE_DURATION_MILLIS = Long.MAX_VALUE
    }
}
