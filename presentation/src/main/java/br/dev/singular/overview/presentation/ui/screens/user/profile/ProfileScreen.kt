package br.dev.singular.overview.presentation.ui.screens.user.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.ActionState
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.UserUiModel
import br.dev.singular.overview.presentation.tagging.params.TagStatus
import br.dev.singular.overview.presentation.ui.components.UiScaffold
import br.dev.singular.overview.presentation.ui.components.button.UiActionButton
import br.dev.singular.overview.presentation.ui.components.button.style.UiActionButtonColors
import br.dev.singular.overview.presentation.ui.components.button.style.UiActionButtonStyle
import br.dev.singular.overview.presentation.ui.components.dialog.UiAlertDialog
import br.dev.singular.overview.presentation.ui.components.navigation.UiTopAppBar
import br.dev.singular.overview.presentation.ui.components.navigation.UiTopAppBarBackPosition.LEADING
import br.dev.singular.overview.presentation.ui.components.person.UiPersonAvatar
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.screens.common.LoadingProgressScreen
import br.dev.singular.overview.presentation.ui.screens.common.StateScreen
import br.dev.singular.overview.presentation.ui.screens.common.TrackScreenView
import br.dev.singular.overview.presentation.ui.screens.user.profile.interaction.ProfileActions
import br.dev.singular.overview.presentation.ui.theme.LowlightColor
import br.dev.singular.overview.presentation.ui.theme.Surface
import br.dev.singular.overview.presentation.ui.theme.SurfaceVariant
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview
import br.dev.singular.overview.presentation.ui.utils.fakeUser

/**
 * A composable that displays the signed-in user's profile and allows signing out after
 * a confirmation.
 *
 * @param uiState The state of the user session. A successful state without a user is an
 * error, unless it is the outcome of the sign-out.
 * @param actionState The state of the sign-out action. Once it succeeds the screen is left.
 * @param actions The actions to be performed on the screen.
 */
@Composable
fun ProfileScreen(
    uiState: UiState<UserUiModel?>,
    actionState: ActionState = ActionState.Idle,
    actions: ProfileActions = ProfileActions()
) {
    UiScaffold(
        topBar = {
            UiTopAppBar(
                title = stringResource(R.string.profile),
                backPosition = LEADING,
                onBack = actions::onBack
            )
        }
    ) { padding ->
        val modifier = Modifier.padding(padding)

        when (uiState) {
            is UiState.Loading -> LoadingProgressScreen(
                tagPath = actions.tagPath,
                modifier = modifier
            )

            is UiState.Error -> ProfileError(tagPath = actions.tagPath, modifier = modifier)

            is UiState.Success -> when (val user = uiState.data) {
                null -> ProfileWithoutUser(actionState, actions, modifier)
                else -> ProfileWithUser(user, actionState, actions, modifier)
            }
        }
    }
}

@Composable
private fun ProfileWithUser(
    user: UserUiModel,
    actionState: ActionState,
    actions: ProfileActions,
    modifier: Modifier
) {
    var showLogoutAlert by rememberSaveable { mutableStateOf(false) }

    TrackScreenView(tagPath = actions.tagPath, status = TagStatus.SUCCESS)
    ProfileContent(
        user = user,
        modifier = modifier,
        enabled = actionState != ActionState.Loading,
        onLogout = {
            actions.onLogoutRequest()
            showLogoutAlert = true
        }
    )
    if (showLogoutAlert) {
        UiAlertDialog(
            title = stringResource(R.string.sign_out_confirm_title),
            message = stringResource(R.string.sign_out_confirm_message),
            confirmText = stringResource(R.string.sign_out),
            onConfirm = {
                showLogoutAlert = false
                actions.onLogout()
            },
            onDismiss = {
                showLogoutAlert = false
                actions.onLogoutCancel()
            }
        )
    }
}

@Composable
private fun ProfileWithoutUser(
    actionState: ActionState,
    actions: ProfileActions,
    modifier: Modifier
) {
    when (actionState) {
        // The session ends slightly before the sign-out action finishes.
        ActionState.Loading -> LoadingProgressScreen(
            tagPath = actions.tagPath,
            modifier = modifier
        )

        ActionState.Success -> LaunchedEffect(Unit) { actions.onSignedOut() }

        else -> ProfileError(tagPath = actions.tagPath, modifier = modifier)
    }
}

@Composable
private fun ProfileError(
    tagPath: String,
    modifier: Modifier
) {
    StateScreen(
        title = stringResource(R.string.error_on_loading),
        tagPath = tagPath,
        tagStatus = TagStatus.ERROR,
        modifier = modifier
    )
}

/**
 * The profile card with the user's picture, name, account and the sign-out button.
 *
 * @param user The signed-in user.
 * @param modifier The modifier to be applied to the content.
 * @param enabled Whether the sign-out button is enabled.
 * @param onLogout The callback to be invoked when the sign-out button is clicked.
 */
@Composable
internal fun ProfileContent(
    user: UserUiModel,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onLogout: () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Surface
            ),
            modifier = Modifier
                .widthIn(max = 400.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.spacing_4x)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UiPersonAvatar(
                    url = user.photoURL,
                    previewDrawableRes = user.previewDrawableRes,
                    size = 160.dp
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_3x)))

                UiTitle(
                    text = user.name
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_2x)))

                GoogleAccountLabel(text = user.email)

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_4x)))

                UiActionButton(
                    text = stringResource(R.string.sign_out),
                    style = UiActionButtonStyle(
                        height = R.dimen.spacing_9x,
                        colors = UiActionButtonColors(
                            activeBorderColor = LowlightColor,
                            activeTextColor = LowlightColor
                        )
                    ),
                    enabled = enabled,
                    onClick = onLogout
                )
            }
        }
    }
}

@Composable
private fun GoogleAccountLabel(
    text: String
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(SurfaceVariant)
            .padding(
                horizontal = dimensionResource(R.dimen.spacing_4x),
                vertical = dimensionResource(R.dimen.spacing_2x)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_2x))
    ) {
        Image(
            painter = painterResource(R.drawable.google_logo),
            contentDescription = null,
            modifier = Modifier.size(dimensionResource(R.dimen.spacing_6x))
        )

        UiText(
            text = text
        )
    }
}

@UiScreenPreview
@Composable
internal fun ProfileScreenPreview() {
    ProfileScreen(uiState = UiState.Success(fakeUser()))
}

@UiScreenPreview
@Composable
internal fun ProfileScreenLoadingPreview() {
    ProfileScreen(uiState = UiState.Loading())
}

@UiScreenPreview
@Composable
internal fun ProfileScreenErrorPreview() {
    ProfileScreen(uiState = UiState.Error())
}
