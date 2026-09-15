package br.dev.singular.overview.presentation.ui.screens.user.login

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.ActionState
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.snackbar.UiSnackbarVisuals
import br.dev.singular.overview.presentation.ui.screens.user.login.interaction.LoginActions
import br.dev.singular.overview.presentation.ui.screens.user.login.interaction.LoginIntent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class LoginScreenTest {

    @get:Rule
    val rule = createComposeRule()

    private val context = RuntimeEnvironment.getApplication()
    private val intents = mutableListOf<LoginIntent>()
    private val errors = mutableListOf<UiSnackbarVisuals>()
    private var idTokenRequests = 0

    private val actions = LoginActions(
        handleIntent = { intents.add(it) },
        onRequestIdToken = { idTokenRequests++ },
        onShowSnackbar = { errors.add(it) }
    )

    @Test
    fun `LoginScreen should start the sign in when the Google button is clicked`() {
        rule.setContent {
            LoginScreen(actionState = ActionState.Idle, actions = actions)
        }

        rule.onNodeWithText(context.getString(R.string.sign_in_with_google)).performClick()

        assertEquals(listOf<LoginIntent>(LoginIntent.Login), intents)
        assertEquals(1, idTokenRequests)
    }

    @Test
    fun `LoginScreen should show the progress and NOT start the sign in while loading`() {
        rule.setContent {
            LoginScreen(actionState = ActionState.Loading, actions = actions)
        }

        rule.onNodeWithText(context.getString(R.string.sign_in_with_google)).performClick()

        rule.onNode(
            hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate),
            useUnmergedTree = true
        ).assertIsDisplayed()
        assertTrue(intents.isEmpty())
        assertEquals(0, idTokenRequests)
    }

    @Test
    fun `LoginScreen should show the error and reset the action when sign in fails`() {
        rule.setContent {
            LoginScreen(actionState = ActionState.Error, actions = actions)
        }
        rule.waitForIdle()

        assertEquals(1, errors.size)
        val visuals = errors.first()
        assertTrue(visuals is UiSnackbarVisuals.Close)
        assertEquals(context.getString(R.string.login_error_title), visuals.title)
        assertEquals(listOf<LoginIntent>(LoginIntent.Reset), intents)
    }
}
