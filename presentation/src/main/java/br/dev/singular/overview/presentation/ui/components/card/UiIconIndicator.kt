package br.dev.singular.overview.presentation.ui.components.card

import androidx.annotation.DimenRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.icon.UiIcon
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.theme.Background
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.List
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Sparkles

private const val CONTAINER_ALPHA = 0.12f

/**
 * A circular indicator that displays an icon over a tinted background.
 *
 * @param icon The icon displayed at the center of the indicator.
 * @param modifier The modifier to be applied to this component.
 * @param sizeRes The diameter of the indicator as a dimension resource.
 * @param iconSizeRes The size of the icon as a dimension resource.
 * @param iconColor The tint color of the icon.
 * @param containerColor The background color of the circle.
 */
@Composable
internal fun UiIconIndicator(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    @DimenRes sizeRes: Int = R.dimen.spacing_20x,
    @DimenRes iconSizeRes: Int = R.dimen.spacing_10x,
    iconColor: Color = HighlightColor,
    containerColor: Color = iconColor.copy(alpha = CONTAINER_ALPHA)
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(dimensionResource(sizeRes))
            .background(
                color = containerColor,
                shape = CircleShape
            )
    ) {
        UiIcon(
            source = UiIconSource.vector(icon),
            color = iconColor,
            modifier = Modifier.size(dimensionResource(iconSizeRes))
        )
    }
}

@UiComponentPreview
@Composable
internal fun UiIconIndicatorPreview() {
    Row(
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_4x))
    ) {
        UiIconIndicator(icon = Lucide.Sparkles)
        UiIconIndicator(
            icon = Lucide.List,
            sizeRes = R.dimen.spacing_12x,
            iconSizeRes = R.dimen.spacing_6x
        )
        UiIconIndicator(
            icon = Lucide.Check,
            sizeRes = R.dimen.spacing_12x,
            iconSizeRes = R.dimen.spacing_6x,
            iconColor = Background,
            containerColor = HighlightColor
        )
    }
}
