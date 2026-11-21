package br.dev.singular.overview.presentation.ui.components.episodes.item

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.utils.fakeEpisode
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiEpisodeItemTest {

    @get:Rule
    val rule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun `should display episode data and trigger click actions`() {
        // arrange
        val mockOnClick = mockk<() -> Unit>(relaxed = true)
        val mockOnToggleWatched = mockk<() -> Unit>(relaxed = true)
        
        val model = fakeEpisode().copy(isWatched = false)

        // act
        rule.setContent {
            UiEpisodeItem(
                model = model,
                onClick = mockOnClick,
                onToggleWatched = mockOnToggleWatched
            )
        }

        // assert
        rule.onNodeWithText(model.episodeNumber).assertIsDisplayed()
        rule.onNodeWithText(model.title).assertIsDisplayed()
        rule.onNodeWithText(model.duration).assertIsDisplayed()

        val markButton = rule.onNodeWithText(context.getString(R.string.episodes_mark))
        markButton.assertIsDisplayed()
        markButton.assertHasClickAction()
        
        // click on toggle button
        markButton.performClick()
        verify(exactly = 1) { mockOnToggleWatched() }

        // click on item (clicking the title text to avoid clicking the inner chip)
        val item = rule.onNode(isCardContainer())
        item.assertHasClickAction()
        item.performTouchInput { click(position = Offset(10f, 10f)) }
        verify(exactly = 1) { mockOnClick() }
    }

    @Test
    fun `should display watched status`() {
        // arrange
        val model = fakeEpisode()

        // act
        rule.setContent {
            UiEpisodeItem(model = model)
        }

        // assert
        rule.onNodeWithText(context.getString(R.string.episodes_watched)).assertIsDisplayed()
    }

    private fun isCardContainer(): SemanticsMatcher =
        SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button)
}
