package br.dev.singular.overview.presentation.ui.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.semantics
import br.dev.singular.overview.presentation.ui.components.button.style.UiButtonStyle
import br.dev.singular.overview.presentation.ui.components.text.UiText

/**
 * A base button component that provides the common structure and styling for all buttons in the app.
 *
 * This internal component handles the surface shape, colors, borders, and height based on the
 * provided [UiButtonStyle]. It also manages a [Row] with a content slot (typically an icon)
 * followed by a bold [UiText].
 *
 * @param text The text to be displayed on the button.
 * @param modifier The [Modifier] to be applied to the button.
 * @param style The [UiButtonStyle] defining the appearance (colors, height, padding, border).
 * @param shape The [Shape] of the button.
 * @param enabled Whether the button is interactive and enabled.
 * @param onClick Callback to be invoked when the button is clicked.
 * @param content A composable slot for additional content (like an icon) displayed before the text.
 *                Provides the current content color to the caller.
 */
@Composable
internal fun UiButtonBase(
    text: String,
    modifier: Modifier,
    style: UiButtonStyle,
    shape: Shape,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val contentColor = style.colors.borderColor(enabled)
    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .height(dimensionResource(style.height)),
        shape = shape,
        color = style.colors.backgroundColor(enabled),
        border = BorderStroke(
            width = dimensionResource(style.border.width),
            color = contentColor
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = dimensionResource(style.horizontalPadding))
                .semantics(mergeDescendants = true) {},
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
            UiText(
                text = text,
                color = style.colors.textColor(enabled),
                isBold = true
            )
        }
    }
}
