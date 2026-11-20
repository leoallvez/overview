package br.dev.singular.overview.presentation.ui.screens.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.snackbar.UiSnackbarVisuals
import br.dev.singular.overview.presentation.ui.components.snackbar.style.UiSnackbarStyle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.WifiOff

/**
 * An effect that notifies the user, through a snackbar, that the internet connection was lost.
 * The snackbar stays on screen until the connection is back.
 *
 * @param isOnline Whether the device is connected to the internet.
 * @param onShow Callback that receives the snackbar to be displayed.
 * @param onDismiss Callback that receives the snackbar to be removed.
 */
@Composable
fun ConnectionLostEffect(
    isOnline: Boolean,
    onShow: (UiSnackbarVisuals) -> Unit,
    onDismiss: (UiSnackbarVisuals) -> Unit
) {
    val title = stringResource(R.string.connection_lost_title)
    val message = stringResource(R.string.connection_lost_message)
    val visuals = remember(title, message) {
        UiSnackbarVisuals.Persistent(
            title = title,
            message = message,
            style = UiSnackbarStyle.Error,
            icon = UiIconSource.vector(Lucide.WifiOff)
        )
    }

    LaunchedEffect(isOnline, visuals) {
        onDismiss(visuals)
        if (!isOnline) {
            onShow(visuals)
        }
    }
}
