package br.dev.singular.overview.presentation.ui.components.text

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerProvider
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiTextTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiText should display the correct text`() {
        val text = "Standard text test"
        rule.setContent {
            UiText(text = text)
        }
        rule.onNodeWithText(text).assertIsDisplayed()
    }

    @Test
    fun `UiTextSkeleton should be rendered`() {
        val tag = "text_skeleton"
        rule.setContent {
            UiShimmerProvider {
                UiTextSkeleton(modifier = Modifier.testTag(tag))
            }
        }
        rule.onNodeWithTag(tag).assertIsDisplayed()
    }
}
