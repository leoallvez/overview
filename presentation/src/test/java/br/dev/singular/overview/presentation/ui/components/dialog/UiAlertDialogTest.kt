package br.dev.singular.overview.presentation.ui.components.dialog

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.R
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiAlertDialogTest {

    @get:Rule
    val rule = createComposeRule()

    private val title = "Alert title"
    private val message = "Alert message"
    private val confirmText = "Yes"
    private val onConfirm: () -> Unit = mockk(relaxed = true)
    private val onDismiss: () -> Unit = mockk(relaxed = true)

    @Test
    fun `UiAlertDialog should display the title, the message and the buttons`() {
        val cancel = RuntimeEnvironment.getApplication().getString(R.string.cancel)

        setContent()

        rule.onNodeWithText(title).assertIsDisplayed()
        rule.onNodeWithText(message).assertIsDisplayed()
        rule.onNodeWithText(confirmText).assertIsDisplayed()
        rule.onNodeWithText(cancel).assertIsDisplayed()
    }

    @Test
    fun `UiAlertDialog should call onConfirm when the confirm button is clicked`() {
        setContent()

        rule.onNodeWithText(confirmText).performClick()

        verify(exactly = 1) { onConfirm() }
        verify(exactly = 0) { onDismiss() }
    }

    @Test
    fun `UiAlertDialog should call onDismiss when the dismiss button is clicked`() {
        val dismissText = "No"

        setContent(dismissText)
        rule.onNodeWithText(dismissText).performClick()

        verify(exactly = 1) { onDismiss() }
        verify(exactly = 0) { onConfirm() }
    }

    private fun setContent(dismissText: String? = null) = rule.setContent {
        if (dismissText == null) {
            UiAlertDialog(
                title = title,
                message = message,
                confirmText = confirmText,
                onConfirm = onConfirm,
                onDismiss = onDismiss
            )
        } else {
            UiAlertDialog(
                title = title,
                message = message,
                confirmText = confirmText,
                dismissText = dismissText,
                onConfirm = onConfirm,
                onDismiss = onDismiss
            )
        }
    }
}
