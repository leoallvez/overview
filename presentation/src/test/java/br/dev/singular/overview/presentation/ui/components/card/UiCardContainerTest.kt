package br.dev.singular.overview.presentation.ui.components.card

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.ui.components.text.UiText
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

private const val CONTENT = "Card content"

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiCardContainerTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `should call onClick when card is clicked`() {
        // arrange
        val onClick: () -> Unit = mockk(relaxed = true)
        rule.setContent {
            UiCardContainer(onClick = onClick) {
                UiText(text = CONTENT)
            }
        }

        // act
        rule.onNodeWithText(CONTENT).performClick()

        // assert
        verify(exactly = 1) { onClick() }
    }

    @Test
    fun `should expose button role`() {
        // act
        rule.setContent {
            UiCardContainer {
                UiText(text = CONTENT)
            }
        }

        // assert
        rule.onNodeWithText(CONTENT)
            .assertHasClickAction()
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button))
    }

    @Test
    fun `should not call onClick when card is disabled`() {
        // arrange
        val onClick: () -> Unit = mockk(relaxed = true)
        rule.setContent {
            UiCardContainer(enabled = false, onClick = onClick) {
                UiText(text = CONTENT)
            }
        }

        // act
        rule.onNodeWithText(CONTENT).performClick()

        // assert
        rule.onNodeWithText(CONTENT)
            .assertIsNotEnabled()
            .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.Role))
        verify(exactly = 0) { onClick() }
    }
}
