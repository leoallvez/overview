package br.dev.singular.overview.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.sharp.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.WarningColor

@Composable
fun UiIcon(
    icon: ImageVector,
    contentDescription: String? = null,
    color: Color = HighlightColor
) {
    Icon(
        tint = color,
        imageVector = icon,
        contentDescription = contentDescription,
        modifier = Modifier.size(dimensionResource(R.dimen.spacing_5x))
    )
}

@Composable
fun UiIcon(
    icon: Painter,
    contentDescription: String? = null,
    color: Color = HighlightColor
) {
    Icon(
        tint = color,
        painter = icon,
        contentDescription = contentDescription,
        modifier = Modifier.size(dimensionResource(R.dimen.spacing_5x))
    )
}

@Preview
@Composable
internal fun UiIconPreview() {
    Column {
        UiIcon(icon = Icons.Filled.Close)
        UiIcon(icon = painterResource(id = R.drawable.ic_outline_alert))
        UiIcon(icon = Icons.Filled.KeyboardArrowDown)
        UiIcon(icon = Icons.Sharp.FavoriteBorder, color = WarningColor)
    }
}
