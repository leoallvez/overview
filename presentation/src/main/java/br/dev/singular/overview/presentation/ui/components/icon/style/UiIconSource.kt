package br.dev.singular.overview.presentation.ui.components.icon.style

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

sealed class UiIconSource {
    data class UiVector(val icon: ImageVector) : UiIconSource()
    data class UiPainter(val painter: Painter) : UiIconSource()

    companion object {
        fun vector(icon: ImageVector) = UiVector(icon)
        fun painter(painter: Painter) = UiPainter(painter)
    }
}

