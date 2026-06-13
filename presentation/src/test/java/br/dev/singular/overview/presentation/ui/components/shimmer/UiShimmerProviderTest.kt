package br.dev.singular.overview.presentation.ui.components.shimmer

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
class UiShimmerProviderTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiShimmerProvider should render its content`() {
        val text = "Shimmer Content"
        rule.setContent {
            UiShimmerProvider {
                UiText(text = text)
            }
        }
        rule.onNodeWithText(text).assertExists()
    }
}
