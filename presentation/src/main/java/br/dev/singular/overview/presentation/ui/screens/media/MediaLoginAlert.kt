package br.dev.singular.overview.presentation.ui.screens.media

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.dialog.UiAlertDialog

/**
 * An alert that tells the user that signing in is required to like a media.
 *
 * @param onConfirm Callback for when the user chooses to sign in.
 * @param onDismiss Callback for when the alert is dismissed.
 */
@Composable
internal fun MediaLoginAlert(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    UiAlertDialog(
        title = stringResource(R.string.login_required_title),
        message = stringResource(R.string.login_required_message),
        confirmText = stringResource(R.string.login),
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}
