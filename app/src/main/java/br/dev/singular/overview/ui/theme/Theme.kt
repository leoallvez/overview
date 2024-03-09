package br.dev.singular.overview.ui.theme

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

// TODO: migrate this for material3
private val AppColorPalette = lightColors(
    primary = Black200,
    primaryVariant = Black200,
    secondary = Black200,
    surface = Black200
)

// TODO: migrate this for material3
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = AppColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
