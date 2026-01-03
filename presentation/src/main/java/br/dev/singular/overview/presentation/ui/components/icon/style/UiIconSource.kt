package br.dev.singular.overview.presentation.ui.components.icon.style

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents the source for an icon, which can be either a vector asset or a drawable resource.
 *
 * This sealed class is used to provide a type-safe way to specify the icon source for UI components.
 *
 * @see UiVector
 * @see UiPainter
 */
sealed class UiIconSource {
    data class UiVector(val imageVector: ImageVector) : UiIconSource()
    data class UiPainter(@param:DrawableRes val drawableRes: Int) : UiIconSource()

    companion object {
        fun vector(icon: ImageVector) = UiVector(icon)
        fun painter(@DrawableRes drawableRes: Int) = UiPainter(drawableRes)
    }
}
