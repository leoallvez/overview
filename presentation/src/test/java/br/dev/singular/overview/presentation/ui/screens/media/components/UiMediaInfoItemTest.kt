package br.dev.singular.overview.presentation.ui.screens.media.components

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
class UiMediaInfoItemTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiMediaInfoItem should display label and value when both are provided`() {
        val label = "Released"
        val value = "12/12/2024"
        rule.setContent {
            UiMediaInfoItem(label = label, value = value)
        }

        rule.onNodeWithText("$label: $value").assertIsDisplayed()
    }

    @Test
    fun `UiMediaInfoItem should display only value when label is blank`() {
        val value = "Item without label"
        rule.setContent {
            UiMediaInfoItem(label = "", value = value)
        }

        rule.onNodeWithText(value).assertIsDisplayed()
    }

    @Test
    fun `UiMediaInfoItem should not display anything when value is blank`() {
        val label = "Label"
        val value = ""
        rule.setContent {
            UiMediaInfoItem(label = label, value = value)
        }

        rule.onNodeWithText("$label:").assertDoesNotExist()
        rule.onNodeWithText(label).assertDoesNotExist()
    }
}
