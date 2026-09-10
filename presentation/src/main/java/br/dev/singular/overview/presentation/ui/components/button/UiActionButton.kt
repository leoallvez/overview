package br.dev.singular.overview.presentation.ui.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.LowlightColor
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.animateActionColor
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Settings2

/**
 * A reusable action button component following the design specifications.
 *
 * @param text The text to be displayed on the button.
 * @param modifier The modifier to be applied to the button.
 * @param icon The optional icon to be displayed before the text.
 * @param enabled Whether the button is enabled and clickable.
 * @param onClick The callback to be invoked when the button is clicked.
 */
@Composable
internal fun UiActionButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: UiIconSource? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val color = animateActionColor(
        isActive = enabled,
        activeColor = HighlightColor,
        inactiveColor = LowlightColor.copy(alpha = 0.3f)
    )
    val backgroundColor = animateActionColor(
        isActive = enabled,
        activeColor = Color.Transparent,
        inactiveColor = LowlightColor.copy(alpha = 0.12f)
    )

    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.button_height)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
        color = backgroundColor,
        contentColor = color,
        border = BorderStroke(
            width = dimensionResource(R.dimen.border_width),
            color = color
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .semantics(mergeDescendants = true) {},
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                val iconSize = dimensionResource(R.dimen.spacing_6x)
                when (icon) {
                    is UiIconSource.UiVector -> Icon(
                        imageVector = icon.imageVector,
                        contentDescription = null,
                        modifier = Modifier.size(iconSize),
                        tint = color
                    )

                    is UiIconSource.UiPainter -> Icon(
                        painter = painterResource(icon.drawableRes),
                        contentDescription = null,
                        modifier = Modifier.size(iconSize),
                        tint = color
                    )
                }
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_2x)))
            }
            UiText(
                text = text,
                color = color,
                isBold = true
            )
        }
    }
}

@UiComponentPreview
@Composable
internal fun UiActionButtonEnabledWithIconPreview() {
    UiActionButton(
        text = "Filter",
        icon = UiIconSource.vector(Lucide.Settings2),
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2x)),
        onClick = {}
    )
}

@UiComponentPreview
@Composable
internal fun UiActionButtonEnabledWithoutIconPreview() {
    UiActionButton(
        text = "Filter",
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2x)),
        onClick = {}
    )
}

@UiComponentPreview
@Composable
internal fun UiActionButtonDisabledWithIconPreview() {
    UiActionButton(
        text = "Filter",
        icon = UiIconSource.vector(Lucide.Settings2),
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2x)),
        enabled = false,
        onClick = {}
    )
}

@UiComponentPreview
@Composable
internal fun UiActionButtonDisabledWithoutIconPreview() {
    UiActionButton(
        text = "Filter",
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2x)),
        enabled = false,
        onClick = {}
    )
}
