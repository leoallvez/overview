package br.dev.singular.overview.presentation.ui.components.snackbar

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiSnackbarTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiSnackBar should call onActionClick when action text is clicked`() {
        val onAction: () -> Unit = mockk(relaxed = true)
        val actionText = "Undo"
        rule.setContent {
            UiSnackbar(
                visuals = UiSnackbarVisuals.Action(
                    title = "Title",
                    message = "Message",
                    actionText = actionText,
                    onAction = onAction
                )
            )
        }
        rule.onNodeWithText(actionText.uppercase()).performClick()
        verify { onAction() }
    }

    @Test
    fun `UiSnackBar should render message correctly`() {
        rule.setContent {
            UiSnackbar(
                visuals = UiSnackbarVisuals.Close(
                    title = "Title",
                    message = "Test Message",
                )
            )
        }
        rule.onNodeWithText("Test Message").assertExists()
    }

    @Test
    fun `UiSnackBar should call onClose when close button is clicked`() {
        val onClose: () -> Unit = mockk(relaxed = true)
        rule.setContent {
            UiSnackbar(
                visuals = UiSnackbarVisuals.Close(
                    title = "Title",
                    message = "Test Message",
                    onClose = onClose,
                )
            )
        }
        rule.onNode(hasClickAction()).performClick()
        verify { onClose() }
    }
}
