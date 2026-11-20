package br.dev.singular.overview.presentation.ui.components.button

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
class UiActionButtonTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiActionButton should call onClick when clicked and enabled`() {
        val onClick: () -> Unit = mockk(relaxed = true)
        val text = "Action Button"
        rule.setContent {
            UiActionButton(text = text, onClick = onClick, enabled = true)
        }
        rule.onNodeWithText(text).performClick()
        verify { onClick() }
    }

    @Test
    fun `UiActionButton should NOT call onClick when clicked and disabled`() {
        val onClick: () -> Unit = mockk(relaxed = true)
        val text = "Action Button"
        rule.setContent {
            UiActionButton(text = text, onClick = onClick, enabled = false)
        }
        rule.onNodeWithText(text).performClick()
        verify(exactly = 0) { onClick() }
    }
}
