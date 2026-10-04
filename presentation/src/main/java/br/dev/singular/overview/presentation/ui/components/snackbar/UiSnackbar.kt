package br.dev.singular.overview.presentation.ui.components.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.UiAnimatedVisibility
import br.dev.singular.overview.presentation.ui.components.icon.UiIcon
import br.dev.singular.overview.presentation.ui.components.icon.UiIconButton
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconStyle
import br.dev.singular.overview.presentation.ui.components.snackbar.style.UiSnackbarStyle
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.border
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.HeartOff
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X

/**
 * A custom SnackBar component designed for dark surface layouts with accent borders, gradient backgrounds, and icons.
 *
 * Rendered based on [UiSnackbarVisuals] variants ([UiSnackbarVisuals.Close] or [UiSnackbarVisuals.Action]).
 *
 * @param visuals The [UiSnackbarVisuals] configuration holding title, message, style, and interactive elements.
 * @param modifier The modifier to be applied to the snackbar.
 * @param visible Controls animated visibility of the snack bar.
 * @param onDismiss Callback invoked when the snackbar requests dismissal (e.g. after action or close).
 */
@Composable
internal fun UiSnackbar(
    visuals: UiSnackbarVisuals,
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    onDismiss: () -> Unit = {}
) {
    UiAnimatedVisibility(
        visible = visible,
        slideFromBottom = true
    ) {
        val shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius))

        Surface(
            modifier = modifier
                .widthIn(max = 560.dp)
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            visuals.style.containerStartColor,
                            visuals.style.containerEndColor
                        )
                    ),
                    shape = shape
                )
                .border(
                    style = UiBorderStyle(
                        color = visuals.style.borderColor,
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
                horizontalArrangement = Arrangement
                    .spacedBy(dimensionResource(R.dimen.spacing_3x))
            ) {

                UiIcon(
                    source = visuals.icon,
                    color = visuals.style.accentColor,
                    modifier = Modifier.size(dimensionResource(R.dimen.spacing_8x))
                )

                Column(modifier = Modifier.weight(1f)) {
                    UiText(
                        text = visuals.title,
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp),
                        color = visuals.style.titleTextColor,
                        isBold = true,
                        textAlign = TextAlign.Start
                    )

                    UiText(
                        text = visuals.message,
                        style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp),
                        color = visuals.style.messageTextColor,
                        textAlign = TextAlign.Start,
                        maxLines = 2
                    )
                }

                when (visuals) {
                    is UiSnackbarVisuals.Action -> {
                        Box(
                            modifier = Modifier
                                .defaultMinSize(
                                    minWidth = dimensionResource(R.dimen.spacing_10x),
                                    minHeight = dimensionResource(R.dimen.spacing_10x)
                                )
                                .clickable {
                                    visuals.onAction()
                                    onDismiss()
                                }
                                .padding(horizontal = dimensionResource(R.dimen.spacing_2x)),
                            contentAlignment = Alignment.Center
                        ) {
                            UiText(
                                text = visuals.actionText.uppercase(),
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp),
                                color = visuals.style.actionTextColor,
                                isBold = true
                            )
                        }
                    }

                    is UiSnackbarVisuals.Close -> {
                        UiIconButton(
                            borderStyle = UiBorderStyle(visible = false),
                            onClick = {
                                visuals.onClose()
                                onDismiss()
                            },
                            background = visuals.style.closeBackgroundColor,
                            iconStyle = UiIconStyle(
                                source = UiIconSource.vector(Lucide.X),
                                color = visuals.style.closeIconColor,
                                sizeRes = R.dimen.spacing_5x
                            )
                        )
                    }
                }
            }
        }
    }
}

@UiComponentPreview
@Composable
internal fun UiSnackbarSuccessPreview() {
    UiSnackbarBasePreview(style = UiSnackbarStyle.Success)
}

@UiComponentPreview
@Composable
internal fun UiSnackbarErrorPreview() {
    UiSnackbarBasePreview(style = UiSnackbarStyle.Error)
}

@Composable
private fun UiSnackbarBasePreview(
    style: UiSnackbarStyle
) {
    Column(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.spacing_4x)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_2x))
    ) {
        UiSnackbar(
            visuals = UiSnackbarVisuals.Close(
                title = stringResource(R.string.lorem_ipsum_short),
                message = stringResource(R.string.lorem_ipsum_long),
                style = style,
                onClose = {},
            )
        )

        UiSnackbar(
            visuals = UiSnackbarVisuals.Action(
                title = stringResource(R.string.lorem_ipsum_short),
                message = stringResource(R.string.lorem_ipsum_long),
                actionText = "undo",
                style = style,
                onAction = {}
            )
        )
    }
}

@UiComponentPreview
@Composable
internal fun UiSnackbarCustomIconPreview() {
    Column(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.spacing_4x)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_2x))
    ) {
        UiSnackbar(
            visuals = UiSnackbarVisuals.Close(
                title = stringResource(R.string.lorem_ipsum_short),
                message = stringResource(R.string.lorem_ipsum_long),
                style = UiSnackbarStyle.Success,
                icon = UiIconSource.vector(Lucide.Heart),
                onClose = {},
            )
        )

        UiSnackbar(
            visuals = UiSnackbarVisuals.Action(
                title = stringResource(R.string.lorem_ipsum_short),
                message = stringResource(R.string.lorem_ipsum_long),
                actionText = "undo",
                style = UiSnackbarStyle.Error,
                icon = UiIconSource.vector(Lucide.HeartOff),
                onAction = {}
            )
        )
    }
}
