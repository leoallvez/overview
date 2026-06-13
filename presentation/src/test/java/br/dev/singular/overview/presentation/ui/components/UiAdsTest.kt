package br.dev.singular.overview.presentation.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiAdsTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiAdsBanner should render when visible`() {
        val tag = "ads_banner"
        rule.setContent {
            UiAdsBanner(
                prodBannerId = R.string.debug_banner,
                isVisible = true,
                modifier = Modifier.testTag(tag)
            )
        }
        rule.onNodeWithTag(tag).assertIsDisplayed()
    }

    @Test
    fun `UiAdsBanner should not render when isVisible is false`() {
        val tag = "ads_banner"
        rule.setContent {
            UiAdsBanner(
                prodBannerId = R.string.debug_banner,
                isVisible = false,
                modifier = Modifier.testTag(tag)
            )
        }
        rule.onNodeWithTag(tag).assertDoesNotExist()
    }

    @Test
    fun `UiAdsMediumRectangle should render when visible`() {
        val tag = "ads_rectangle"
        rule.setContent {
            UiAdsMediumRectangle(
                prodBannerId = R.string.debug_banner,
                isVisible = true,
                modifier = Modifier.testTag(tag)
            )
        }
        rule.onNodeWithTag(tag).assertIsDisplayed()
    }

    @Test
    fun `UiAdsMediumRectangle should not render when isVisible is false`() {
        val tag = "ads_rectangle"
        rule.setContent {
            UiAdsMediumRectangle(
                prodBannerId = R.string.debug_banner,
                isVisible = false,
                modifier = Modifier.testTag(tag)
            )
        }
        rule.onNodeWithTag(tag).assertDoesNotExist()
    }
}
