package br.dev.singular.overview.presentation.ui.components.tooltip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.UiAnimatedVisibility
import br.dev.singular.overview.presentation.ui.components.icon.UiIcon
import br.dev.singular.overview.presentation.ui.components.icon.UiIconButton
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconStyle
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.components.tooltip.style.UiTooltipStyle
import br.dev.singular.overview.presentation.ui.theme.WarningColor
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.border
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/**
 * A composable that displays an information tooltip with a message and an optional close button.
 *
 * @param message The message to be displayed in the tooltip.
 * @param modifier The modifier to be applied to the tooltip.
 * @param icon The style of the icon to be displayed.
 * @param visible Controls animated visibility of the tooltip.
 * @param onClose A callback to be invoked when the close button is clicked.
 */
@Composable
internal fun UiTooltip(
    message: String,
    modifier: Modifier = Modifier,
    style: UiTooltipStyle = UiTooltipStyle(),
    icon: UiIconStyle? = null,
    visible: Boolean = true,
    onClose: () -> Unit = {}
) {
    TooltipContent(
        modifier = modifier,
        style = style,
        visible = visible,
        message = message,
        icon = icon,
        onClose = onClose,
        showCloseButton = true
    )
}

@Composable
internal fun UiTooltip(
    message: String,
    modifier: Modifier = Modifier,
    style: UiTooltipStyle = UiTooltipStyle(),
    icon: UiIconStyle? = null,
    visible: Boolean = true
) {
    TooltipContent(
        modifier = modifier,
        style = style,
        visible = visible,
        message = message,
        icon = icon,
        showCloseButton = false
    )
}

@Composable
private fun TooltipContent(
    modifier: Modifier = Modifier,
    style: UiTooltipStyle,
    message: String,
    icon: UiIconStyle? = null,
    visible: Boolean = true,
    showCloseButton: Boolean = false,
    onClose: () -> Unit = {}
) {
    UiAnimatedVisibility(visible) {
        val resolvedIcon = icon ?: UiIconStyle(
            source = style.defaultIcon,
            color = style.accentColor
        )

        val shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius))

        Surface(
            modifier = modifier
                .widthIn(max = 560.dp)
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            style.containerStartColor,
                            style.containerEndColor
                        )
                    ),
                    shape = shape
                )
                .border(
                    style = UiBorderStyle(
                        color = style.borderColor,
                        shape = shape
                    )
                ),
            shape = shape,
            color = Color.Transparent,
            tonalElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .defaultMinSize(minHeight = dimensionResource(R.dimen.button_height))
                    .padding(dimensionResource(R.dimen.spacing_4x)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_3x))
            ) {
                UiIcon(
                    source = resolvedIcon.source,
                    color = resolvedIcon.color,
                    modifier = Modifier.size(dimensionResource(R.dimen.spacing_8x))
                )

                UiText(
                    text = message,
                    modifier = Modifier
                        .weight(1f),
                    style = MaterialTheme.typography.bodyMedium,
                    color = style.messageTextColor,
                    textAlign = TextAlign.Start,
                )

                if (showCloseButton) {
                    UiIconButton(
                        borderStyle = UiBorderStyle(visible = false),
                        onClick = onClose,
                        background = style.closeBackgroundColor,
                        iconStyle = UiIconStyle(
                            source = UiIconSource.vector(Lucide.X),
                            color = style.closeIconColor,
                            sizeRes = R.dimen.spacing_5x,
                            descriptionRes = R.string.close
                        )
                    )
                }
            }
        }
    }
}

@UiComponentPreview
@Composable
internal fun UiTooltipPreview() {
    val visible = remember { mutableStateOf(true) }
    UiTooltip(
        visible = visible.value,
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x)),
        message = stringResource(R.string.lorem_ipsum_long)
    ) {
        visible.value = false
    }
}

@UiComponentPreview
@Composable
internal fun UiTooltipWithoutIconPreview() {
    UiTooltip(
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x)),
        message = stringResource(R.string.lorem_ipsum_long)
    )
}

@UiComponentPreview
@Composable
internal fun UiTooltipWithCustomIconPreview() {
    UiTooltip(
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x)),
        icon = UiIconStyle(
            source = UiIconSource.vector(icon = Lucide.Heart),
            color = WarningColor,
        ),
        message = stringResource(R.string.lorem_ipsum_long)
    )
}

@UiComponentPreview
@Composable
internal fun UiTooltipAnimationPreview() {
    // Active Interactive mode to see the tooltip animation

    val visible = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            visible.value = !visible.value
            delay(2000.milliseconds)
        }
    }

    Box(
        modifier = Modifier
            .width(500.dp)
            .height(130.dp)
            .padding(dimensionResource(R.dimen.spacing_4x))
    ) {
        UiTooltip(
            visible = visible.value,
            icon = UiIconStyle(
                source = UiIconSource.vector(icon = Lucide.Heart),
                color = WarningColor,
            ),
            onClose = {},
            message = stringResource(R.string.lorem_ipsum_long)
        )
    }
}
