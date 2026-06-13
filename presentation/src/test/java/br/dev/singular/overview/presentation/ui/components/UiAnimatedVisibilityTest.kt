package br.dev.singular.overview.presentation.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.ui.components.text.UiText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiAnimatedVisibilityTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiAnimatedVisibility should hide content when visible is false`() {
        val text = "You can't see me!"
        rule.setContent {
            UiAnimatedVisibility(visible = false) {
                UiText(text = text)
            }
        }
        rule.onNodeWithText(text).assertDoesNotExist()
    }

    @Test
    fun `UiAnimatedVisibility should show content when visible is true`() {
        val text = "Hello!"
        rule.setContent {
            UiAnimatedVisibility(visible = true) {
                UiText(text = text)
            }
        }
        rule.onNodeWithText(text).assertIsDisplayed()
    }
}
