package br.dev.singular.overview.presentation.ui.components.icon.style

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

sealed class UiIconSource {
    data class UiVector(val imageVector: ImageVector) : UiIconSource()
    data class UiPainter(@param:DrawableRes val drawableRes: Int) : UiIconSource()

    companion object {
        fun vector(icon: ImageVector) = UiVector(icon)
        fun painter(@DrawableRes drawableRes: Int) = UiPainter(drawableRes)
    }
}
