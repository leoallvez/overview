package br.dev.singular.overview.presentation.ui.screens.user.profile

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.ActionState
import br.dev.singular.overview.presentation.NavigationWrapperMock
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.UserUiModel
import br.dev.singular.overview.presentation.ui.screens.user.login.interaction.LoginIntent
import br.dev.singular.overview.presentation.ui.screens.user.profile.interaction.ProfileActions
import br.dev.singular.overview.presentation.ui.utils.fakeUser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class ProfileScreenTest {

    @get:Rule
    val rule = createComposeRule()

    private val context = RuntimeEnvironment.getApplication()
    private val navigation = NavigationWrapperMock()
    private val intents = mutableListOf<LoginIntent>()

    private val actions = ProfileActions(
        navigation = navigation,
        handleIntent = { intents.add(it) }
    )

    private val signOut = context.getString(R.string.sign_out)
    private val confirmTitle = context.getString(R.string.sign_out_confirm_title)
    private val errorOnLoading = context.getString(R.string.error_on_loading)

    @Test
    fun `ProfileScreen should display the user and ask for confirmation before signing out`() {
        val user = fakeUser()
        rule.setContent {
            ProfileScreen(uiState = UiState.Success(user), actions = actions)
        }

        rule.onNodeWithText(user.name).assertIsDisplayed()
        rule.onNodeWithText(user.email).assertIsDisplayed()
        rule.onNodeWithText(signOut).performClick()

        rule.onNodeWithText(confirmTitle).assertIsDisplayed()
        assertTrue(intents.isEmpty())
    }

    @Test
    fun `ProfileScreen should sign out when the alert is confirmed`() {
        rule.setContent {
            ProfileScreen(uiState = UiState.Success(fakeUser()), actions = actions)
        }

        rule.onNodeWithText(signOut).performClick()
        rule.onNode(hasText(signOut) and hasAnyAncestor(isDialog())).performClick()

        assertEquals(listOf<LoginIntent>(LoginIntent.Logout), intents)
        assertFalse(navigation.wasPopBackStackCalled)
        rule.onNodeWithText(confirmTitle).assertDoesNotExist()
    }

    @Test
    fun `ProfileScreen should NOT sign out when the alert is cancelled`() {
        rule.setContent {
            ProfileScreen(uiState = UiState.Success(fakeUser()), actions = actions)
        }

        rule.onNodeWithText(signOut).performClick()
        rule.onNodeWithText(context.getString(R.string.cancel)).performClick()

        assertTrue(intents.isEmpty())
        rule.onNodeWithText(confirmTitle).assertDoesNotExist()
    }

    @Test
    fun `ProfileScreen should NOT sign out when clicked while loading`() {
        rule.setContent {
            ProfileScreen(
                uiState = UiState.Success(fakeUser()),
                actionState = ActionState.Loading,
                actions = actions
            )
        }

        rule.onNodeWithText(signOut).performClick()

        assertTrue(intents.isEmpty())
        rule.onNodeWithText(confirmTitle).assertDoesNotExist()
    }

    @Test
    fun `ProfileScreen should navigate back when the back button is clicked`() {
        rule.setContent {
            ProfileScreen(uiState = UiState.Loading(), actions = actions)
        }

        rule.onNode(hasClickAction()).performClick()

        assertTrue(navigation.wasPopBackStackCalled)
    }

    @Test
    fun `ProfileScreen should show the error when there is no user to display`() {
        rule.setContent {
            ProfileScreen(uiState = UiState.Success<UserUiModel?>(null), actions = actions)
        }
        rule.waitForIdle()

        rule.onNodeWithText(errorOnLoading).assertIsDisplayed()
        assertFalse(navigation.wasPopBackStackCalled)
    }

    @Test
    fun `ProfileScreen should show the error when the session fails to load`() {
        rule.setContent {
            ProfileScreen(uiState = UiState.Error(), actions = actions)
        }

        rule.onNodeWithText(errorOnLoading).assertIsDisplayed()
    }

    @Test
    fun `ProfileScreen should NOT show the error while the sign out is in progress`() {
        rule.setContent {
            ProfileScreen(
                uiState = UiState.Success<UserUiModel?>(null),
                actionState = ActionState.Loading,
                actions = actions
            )
        }
        rule.waitForIdle()

        rule.onNodeWithText(errorOnLoading).assertDoesNotExist()
        assertFalse(navigation.wasPopBackStackCalled)
    }

    @Test
    fun `ProfileScreen should navigate back when the sign out succeeds`() {
        rule.setContent {
            ProfileScreen(
                uiState = UiState.Success<UserUiModel?>(null),
                actionState = ActionState.Success,
                actions = actions
            )
        }
        rule.waitForIdle()

        rule.onNodeWithText(errorOnLoading).assertDoesNotExist()
        assertTrue(navigation.wasPopBackStackCalled)
    }
}
