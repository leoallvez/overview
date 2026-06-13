package br.dev.singular.overview.presentation.ui.components.icon

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.WarningColor
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X

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

@UiComponentPreview
@Composable
internal fun UiIconPreview() {
    Column(
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2x))
    ) {
        UiIcon(source = UiIconSource.UiVector(Lucide.X))
        UiIcon(source = UiIconSource.UiPainter(R.drawable.ic_outline_alert))
        UiIcon(source = UiIconSource.UiVector(Lucide.ChevronDown))
        UiIcon(source = UiIconSource.UiVector(Lucide.Heart), color = WarningColor)
    }
}
