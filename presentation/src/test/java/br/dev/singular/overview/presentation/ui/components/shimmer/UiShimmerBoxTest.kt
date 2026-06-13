package br.dev.singular.overview.presentation.ui.components.shimmer

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiShimmerBoxTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiShimmerBox should be rendered`() {
        val tag = "shimmer_box"
        rule.setContent {
            UiShimmerProvider {
                UiShimmerBox(modifier = Modifier.size(100.dp).testTag(tag))
            }
        }
        rule.onNodeWithTag(tag).assertIsDisplayed()
    }
}
