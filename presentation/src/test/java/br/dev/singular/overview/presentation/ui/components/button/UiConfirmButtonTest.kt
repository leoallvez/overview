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
class UiConfirmButtonTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiConfirmButton should call onClick when clicked and enabled`() {
        val onClick: () -> Unit = mockk(relaxed = true)
        
        rule.setContent {
            UiConfirmButton(enabled = true, onClick = onClick)
        }
        
        // "Confirm" is the value of R.string.confirm in values/strings.xml
        rule.onNodeWithText("Confirm").performClick()
        
        verify { onClick() }
    }

    @Test
    fun `UiConfirmButton should NOT call onClick when clicked and disabled`() {
        val onClick: () -> Unit = mockk(relaxed = true)
        
        rule.setContent {
            UiConfirmButton(enabled = false, onClick = onClick)
        }
        
        rule.onNodeWithText("Confirm").performClick()
        
        verify(exactly = 0) { onClick() }
    }
}
