package br.dev.singular.overview.presentation.ui.screens.user.login

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.ActionState
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.snackbar.UiSnackbarVisuals
import br.dev.singular.overview.presentation.ui.components.snackbar.style.UiSnackbarStyle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class LoginErrorEffectTest {

    @get:Rule
    val rule = createComposeRule()

    private val context = RuntimeEnvironment.getApplication()
    private val snackbars = mutableListOf<UiSnackbarVisuals>()

    @Test
    fun `LoginErrorEffect should show the error snackbar when the sign in fails`() {
        rule.setContent {
            LoginErrorEffect(
                actionState = ActionState.Error,
                onShow = { snackbars.add(it) }
            )
        }
        rule.waitForIdle()

        assertEquals(1, snackbars.size)
        val visuals = snackbars.first()
        assertTrue(visuals is UiSnackbarVisuals.Close)
        assertEquals(context.getString(R.string.login_error_title), visuals.title)
        assertEquals(context.getString(R.string.login_error_message), visuals.message)
        assertEquals(UiSnackbarStyle.Error, visuals.style)
    }

    @Test
    fun `LoginErrorEffect should not show the snackbar when the sign in has not failed`() {
        rule.setContent {
            listOf(ActionState.Idle, ActionState.Loading, ActionState.Success).forEach { state ->
                LoginErrorEffect(
                    actionState = state,
                    onShow = { snackbars.add(it) }
                )
            }
        }
        rule.waitForIdle()

        assertTrue(snackbars.isEmpty())
    }
}
