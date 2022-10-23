package io.github.leoallvez.take.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val TakeColorPalette = lightColors(
    primary = Black200,
    primaryVariant = Black200,
    secondary = Black200,
    surface = Black200
)

@Composable
fun TakeTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colors = TakeColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
