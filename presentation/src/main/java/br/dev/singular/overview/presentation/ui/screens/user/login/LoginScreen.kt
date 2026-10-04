package br.dev.singular.overview.presentation.ui.screens.user.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.ActionState
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.UiScaffold
import br.dev.singular.overview.presentation.ui.components.button.UiGoogleButton
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconStyle
import br.dev.singular.overview.presentation.ui.components.navigation.UiTopAppBar
import br.dev.singular.overview.presentation.ui.components.tooltip.UiTooltip
import br.dev.singular.overview.presentation.ui.screens.common.TrackScreenView
import br.dev.singular.overview.presentation.ui.screens.user.login.interaction.LoginActions
import br.dev.singular.overview.presentation.ui.theme.WarningColor
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview
import br.dev.singular.overview.presentation.ui.utils.UiSnackbarPreview
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.Lucide
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/**
 * A composable that invites the user to sign in with a Google account.
 *
 * @param actionState The state of the sign in action.
 * @param actions The actions to be performed on the screen.
 */
@Composable
fun LoginScreen(
    actionState: ActionState,
    actions: LoginActions = LoginActions()
) {
    TrackScreenView(tagPath = actions.tagPath)

    LoginErrorEffect(
        actionState = actionState,
        onShow = actions::onError
    )

    UiScaffold(
        topBar = {
            UiTopAppBar(
                title = stringResource(R.string.login)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.TopCenter
        ) {
            UiTooltip(
                icon = UiIconStyle(
                    source = UiIconSource.vector(icon = Lucide.Heart),
                    color = WarningColor,
                ),
                message = stringResource(R.string.login_info_message),
            )
            UiGoogleButton(
                onClick = actions::onLogin,
                modifier = Modifier.align(Alignment.Center),
                loading = actionState == ActionState.Loading
            )
        }
    }
}

@UiScreenPreview
@Composable
internal fun LoginScreenPreview() {
    LoginScreen(actionState = ActionState.Idle)
}

@UiScreenPreview
@Composable
internal fun LoginScreenLoadingPreview() {
    LoginScreen(actionState = ActionState.Loading)
}

@UiScreenPreview
@Composable
internal fun LoginScreenErrorPreview() {

    // Active Interactive mode to see the snackbar
    var actionState by remember {
        mutableStateOf<ActionState>(ActionState.Idle)
    }

    LaunchedEffect(Unit) {
        while (true) {
            actionState = ActionState.Error
            delay(5_000.milliseconds)
            actionState = ActionState.Idle
            delay(1_000.milliseconds)
        }
    }

    UiSnackbarPreview { hostState ->
        LoginScreen(
            actionState = actionState,
            actions = LoginActions(onShowSnackbar = hostState::show)
        )
    }
}
