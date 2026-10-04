package br.dev.singular.overview.presentation.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.button.UiActionButton
import br.dev.singular.overview.presentation.ui.components.button.style.UiActionButtonColors
import br.dev.singular.overview.presentation.ui.components.button.style.UiActionButtonStyle
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.theme.LowlightColor
import br.dev.singular.overview.presentation.ui.theme.SubtitleTextColor
import br.dev.singular.overview.presentation.ui.theme.Surface
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview

/**
 * A modal alert that asks the user to confirm or dismiss an action.
 *
 * Touching outside the alert or pressing back invokes [onDismiss].
 *
 * @param title The title of the alert.
 * @param message The message explaining what is being confirmed.
 * @param confirmText The text of the button that confirms the action.
 * @param modifier The modifier to be applied to the alert.
 * @param dismissText The text of the button that dismisses the alert.
 * @param onConfirm The callback to be invoked when the confirm button is clicked.
 * @param onDismiss The callback to be invoked when the alert is dismissed.
 */
@Composable
internal fun UiAlertDialog(
    title: String,
    message: String,
    confirmText: String,
    modifier: Modifier = Modifier,
    dismissText: String = stringResource(R.string.cancel),
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    Dialog(onDismissRequest = onDismiss) {
        UiAlertDialogContent(
            title = title,
            message = message,
            confirmText = confirmText,
            modifier = modifier,
            dismissText = dismissText,
            onConfirm = onConfirm,
            onDismiss = onDismiss
        )
    }
}

/**
 * The content of [UiAlertDialog], without the dialog window.
 *
 * @param title The title of the alert.
 * @param message The message explaining what is being confirmed.
 * @param confirmText The text of the button that confirms the action.
 * @param modifier The modifier to be applied to the content.
 * @param dismissText The text of the button that dismisses the alert.
 * @param onConfirm The callback to be invoked when the confirm button is clicked.
 * @param onDismiss The callback to be invoked when the dismiss button is clicked.
 */
@Composable
internal fun UiAlertDialogContent(
    title: String,
    message: String,
    confirmText: String,
    modifier: Modifier = Modifier,
    dismissText: String = stringResource(R.string.cancel),
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val buttonStyle = UiActionButtonStyle(height = R.dimen.spacing_11x)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.spacing_6x))
        ) {
            Column(
                modifier = Modifier.semantics(mergeDescendants = true) {}
            ) {
                UiTitle(text = title)

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_2x)))

                UiText(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SubtitleTextColor,
                    textAlign = TextAlign.Start,
                    maxLines = MESSAGE_MAX_LINES
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_6x)))

            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_3x))
            ) {
                UiActionButton(
                    text = dismissText,
                    modifier = Modifier.weight(1f),
                    style = buttonStyle.copy(
                        colors = UiActionButtonColors(
                            activeBorderColor = LowlightColor,
                            activeTextColor = LowlightColor
                        )
                    ),
                    onClick = onDismiss
                )
                UiActionButton(
                    text = confirmText,
                    modifier = Modifier.weight(1f),
                    style = buttonStyle,
                    onClick = onConfirm
                )
            }
        }
    }
}

private const val MESSAGE_MAX_LINES = 6

@UiComponentPreview
@Composable
internal fun UiAlertDialogPreview() {
    UiAlertDialogContent(
        title = stringResource(R.string.sign_out_confirm_title),
        message = stringResource(R.string.sign_out_confirm_message),
        confirmText = stringResource(R.string.sign_out),
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    )
}

@UiComponentPreview
@Composable
internal fun UiAlertDialogLongTextPreview() {
    UiAlertDialogContent(
        title = stringResource(R.string.lorem_ipsum_long),
        message = stringResource(R.string.lorem_ipsum_long),
        confirmText = stringResource(R.string.confirm),
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    )
}
