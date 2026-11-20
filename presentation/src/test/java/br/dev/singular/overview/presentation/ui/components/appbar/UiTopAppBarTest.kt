package br.dev.singular.overview.presentation.ui.components.appbar

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.ui.components.navigation.UiTopAppBar
import br.dev.singular.overview.presentation.ui.components.navigation.UiTopAppBarBackPosition
import br.dev.singular.overview.presentation.ui.components.text.UiText
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
    fun `UiTopAppBar with leading back button should display title and respond to click`() {
        assertBackBehavior(position = UiTopAppBarBackPosition.LEADING)
    }

    @Test
    fun `UiTopAppBar with trailing back button should display title and respond to click`() {
        assertBackBehavior(position = UiTopAppBarBackPosition.TRAILING)
    }

    @Test
    fun `UiTopAppBar with trailing content should display title and content`() {
        val title = "Toolbar Title"
        rule.setContent {
            UiTopAppBar(
                title = title,
                trailingContent = { UiText(text = "Trailing") }
            )
        }
        rule.onNodeWithText(title).assertIsDisplayed()
        rule.onNodeWithText("Trailing").assertIsDisplayed()
    }

    private fun assertBackBehavior(
        position: UiTopAppBarBackPosition
    ) {
        val title = "Back Title"
        val onBack: () -> Unit = mockk(relaxed = true)
        rule.setContent {
            UiTopAppBar(
                title = title, onBack = onBack,
                backPosition = position
            )
        }
        rule.onNodeWithText(title).assertIsDisplayed()
        rule.onNode(hasClickAction()).performClick()
        verify { onBack() }
    }
}
