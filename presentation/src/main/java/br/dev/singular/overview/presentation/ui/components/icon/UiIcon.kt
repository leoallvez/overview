package br.dev.singular.overview.presentation.ui.components.icon

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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.WarningColor

/**
 * A composable that displays an icon from a [UiIconSource].
 *
 * @param source The source of the icon, either a [UiIconSource.UiVector] or [UiIconSource.UiPainter].
 * @param modifier The modifier to be applied to this icon.
 * @param contentDescription The content description for accessibility.
 * @param color The tint color to be applied to the icon.
 */
@Composable
fun UiIcon(
    source: UiIconSource,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    color: Color = HighlightColor
) {
    val size = dimensionResource(R.dimen.spacing_5x)
    when (source) {
        is UiIconSource.UiVector -> Icon(
            tint = color,
            imageVector = source.imageVector,
            contentDescription = contentDescription,
            modifier = modifier.size(size)
        )
        is UiIconSource.UiPainter -> Icon(
            tint = color,
            painter = painterResource(source.drawableRes),
            contentDescription = contentDescription,
            modifier = modifier.size(size)
        )
    }
}

@Preview
@Composable
internal fun UiIconPreview() {
    Column {
        UiIcon(source = UiIconSource.UiVector(Icons.Filled.Close))
        UiIcon(source = UiIconSource.UiPainter(R.drawable.ic_outline_alert))
        UiIcon(source = UiIconSource.UiVector(Icons.Filled.KeyboardArrowDown))
        UiIcon(source = UiIconSource.UiVector(Icons.Sharp.FavoriteBorder), color = WarningColor)
    }
}
