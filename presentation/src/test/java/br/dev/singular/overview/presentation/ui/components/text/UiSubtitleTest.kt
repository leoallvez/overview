package br.dev.singular.overview.presentation.ui.components.text

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiSubtitleTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiSubtitle should display the correct text when display is true`() {
        val text = "Subtitle Test"
        rule.setContent {
            UiSubtitle(text = text, display = true)
        }
        rule.onNodeWithText(text).assertIsDisplayed()
    }

    @Test
    fun `UiSubtitle should not display anything when display is false`() {
        val text = "Hidden Subtitle"
        rule.setContent {
            UiSubtitle(text = text, display = false)
        }
        rule.onNodeWithText(text).assertDoesNotExist()
    }
}
