package br.dev.singular.overview.presentation.ui.screens.user.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.ActionState
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.snackbar.UiSnackbarVisuals
import br.dev.singular.overview.presentation.ui.components.snackbar.style.UiSnackbarStyle

/**
 * An effect that notifies the user, through a snackbar, that the sign in has failed.
 *
 * @param actionState The state of the sign in action.
 * @param onShow Callback that receives the snackbar to be displayed.
 */
@Composable
internal fun LoginErrorEffect(
    actionState: ActionState,
    onShow: (UiSnackbarVisuals) -> Unit
) {
    val title = stringResource(R.string.login_error_title)
    val message = stringResource(R.string.login_error_message)

    LaunchedEffect(actionState) {
        if (actionState is ActionState.Error) {
            onShow(
                UiSnackbarVisuals.Close(
                    title = title,
                    message = message,
                    style = UiSnackbarStyle.Error
                )
            )
        }
    }
}
