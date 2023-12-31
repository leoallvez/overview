package br.dev.singular.overview.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val AppColorPalette = lightColors(
    primary = Black200,
    primaryVariant = Black200,
    secondary = Black200,
    surface = Black200
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = AppColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
