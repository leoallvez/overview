package br.dev.singular.overview.presentation.ui.components

import androidx.compose.ui.test.assertIsDisplayed
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
class UiTopAppBarTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiTopAppBar should display title`() {
        val title = "Toolbar Title"
        rule.setContent {
            UiTopAppBar(title = title)
        }
        rule.onNodeWithText(title).assertIsDisplayed()
    }

    @Test
    fun `UiTopAppBar with back button should display title and respond to click`() {
        val title = "Back Title"
        val onBack: () -> Unit = mockk(relaxed = true)
        rule.setContent {
            UiTopAppBar(title = title, onBack = onBack)
        }
        rule.onNodeWithText(title).assertIsDisplayed()
        rule.onNode(hasClickAction()).performClick()
        verify { onBack() }
    }
}
