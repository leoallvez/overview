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
class UiParagraphTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiParagraph should display the correct text`() {
        val text = "Paragraph content for testing purposes."
        rule.setContent {
            UiParagraph(text = text)
        }
        rule.onNodeWithText(text).assertIsDisplayed()
    }
}
