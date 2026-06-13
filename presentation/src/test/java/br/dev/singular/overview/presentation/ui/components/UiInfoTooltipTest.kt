package br.dev.singular.overview.presentation.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.R
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.RuntimeEnvironment

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiInfoTooltipTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiInfoTooltip should be visible and call onClose when close button clicked`() {
        val onClose: () -> Unit = mockk(relaxed = true)
        val message = "Info message"
        val closeDescription = RuntimeEnvironment.getApplication().getString(R.string.close)
        
        rule.setContent {
            UiInfoTooltip(message = message, visible = true, onClose = onClose)
        }
        
        rule.onNodeWithText(message).assertIsDisplayed()
        rule.onNodeWithContentDescription(closeDescription).performClick()
        verify { onClose() }
    }

    @Test
    fun `UiInfoTooltip should not be visible when visible is false`() {
        val message = "Hidden message"
        rule.setContent {
            UiInfoTooltip(message = message, visible = false)
        }
        rule.onNodeWithText(message).assertDoesNotExist()
    }
}
