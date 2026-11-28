package br.dev.singular.overview.presentation.ui.components.episodes.chip

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [33])
class UiWatchedChipTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `should display Watched when isWatched is true`() {
        rule.setContent {
            UiWatchedChip(isWatched = true)
        }

        rule.onNodeWithText("Watched").assertIsDisplayed()
        rule.onNodeWithText("Mark").assertDoesNotExist()
    }

    @Test
    fun `should display Mark when isWatched is false`() {
        rule.setContent {
            UiWatchedChip(isWatched = false)
        }

        rule.onNodeWithText("Mark").assertIsDisplayed()
        rule.onNodeWithText("Watched").assertDoesNotExist()
    }

    @Test
    fun `should trigger onClick when chip is clicked`() {
        var clicked = false

        rule.setContent {
            UiWatchedChip(
                isWatched = false,
                onClick = { clicked = true }
            )
        }

        rule.onNodeWithText("Mark").performClick()

        assertTrue(clicked)
    }

    @Test
    fun `skeleton should render without content text`() {
        val tag = "watched_chip_skeleton"
        rule.setContent {
            UiWatchedChipSkeleton(
                modifier = Modifier.testTag(tag)
            )
        }

        // Verify skeleton structure exists
        rule.onNodeWithTag(tag).assertIsDisplayed()
        
        // Verify real content is NOT present
        rule.onNodeWithText("Watched").assertDoesNotExist()
        rule.onNodeWithText("Mark").assertDoesNotExist()
    }
}
