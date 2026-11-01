package br.dev.singular.overview.presentation.ui.components.episodes

import androidx.annotation.StringRes
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.EpisodesProgressUiModel
import br.dev.singular.overview.presentation.ui.utils.fakeEpisodesSummary
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiEpisodesCardTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `should display seasons and episodes when show has many seasons`() {
        // act
        rule.setContent {
            UiEpisodesCard(
                model = fakeEpisodesSummary().copy(numberOfSeasons = 5, numberOfEpisodes = 62),
                progress = EpisodesProgressUiModel.NotStarted
            )
        }

        // assert
        rule.onNodeWithText(string(R.string.episodes_title)).assertIsDisplayed()
        rule.onNodeWithText("5 seasons • 62 episodes").assertIsDisplayed()
    }

    @Test
    fun `should merge texts into a single clickable button node`() {
        // act
        rule.setContent {
            UiEpisodesCard(
                model = fakeEpisodesSummary().copy(numberOfSeasons = 5, numberOfEpisodes = 62),
                progress = EpisodesProgressUiModel.NotStarted
            )
        }

        // assert
        rule.onNodeWithText(string(R.string.episodes_title))
            .assertHasClickAction()
            .assertTextContains("5 seasons • 62 episodes")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button))
    }

    @Test
    fun `should call onClick when card is clicked`() {
        // arrange
        val onClick: () -> Unit = mockk(relaxed = true)
        rule.setContent {
            UiEpisodesCard(
                model = fakeEpisodesSummary(),
                progress = EpisodesProgressUiModel.NotStarted,
                onClick = onClick
            )
        }

        // act
        rule.onNodeWithText(string(R.string.episodes_title)).performClick()

        // assert
        verify(exactly = 1) { onClick() }
    }

    @Test
    fun `should display sign in hint when user is signed out`() {
        // act
        rule.setContent {
            UiEpisodesCard(
                model = fakeEpisodesSummary(),
                progress = EpisodesProgressUiModel.SignedOut
            )
        }

        // assert
        rule.onNodeWithText(string(R.string.episodes_sign_in_hint)).assertIsDisplayed()
    }

    @Test
    fun `should not display sign in hint when user has not started the show`() {
        // act
        rule.setContent {
            UiEpisodesCard(
                model = fakeEpisodesSummary(),
                progress = EpisodesProgressUiModel.NotStarted
            )
        }

        // assert
        rule.onNodeWithText(string(R.string.episodes_sign_in_hint)).assertDoesNotExist()
    }

    @Test
    fun `should display seasons and episodes with progress percentage when show is in progress`() {
        // act
        rule.setContent {
            UiEpisodesCard(
                model = fakeEpisodesSummary().copy(numberOfSeasons = 5, numberOfEpisodes = 62),
                progress = EpisodesProgressUiModel.InProgress(progress = 0.5f)
            )
        }

        // assert
        rule.onNodeWithText(string(R.string.episodes_title)).assertIsDisplayed()
        rule.onNodeWithText("5 seasons • 62 episodes").assertIsDisplayed()
        rule.onNodeWithText("50%").assertIsDisplayed()
        rule.onNode(
            hasProgressBarRangeInfo(ProgressBarRangeInfo(0.5f, 0f..1f)),
            useUnmergedTree = true
        ).assertExists()
    }

    @Test
    fun `should call onClick when card is clicked while show is in progress`() {
        // arrange
        val onClick: () -> Unit = mockk(relaxed = true)
        rule.setContent {
            UiEpisodesCard(
                model = fakeEpisodesSummary(),
                progress = EpisodesProgressUiModel.InProgress(progress = 0.08f),
                onClick = onClick
            )
        }

        // act
        rule.onNodeWithText(string(R.string.episodes_title)).performClick()

        // assert
        verify(exactly = 1) { onClick() }
    }

    @Test
    fun `should display seasons and episodes with full progress when show is completed`() {
        // act
        rule.setContent {
            UiEpisodesCard(
                model = fakeEpisodesSummary().copy(numberOfSeasons = 5, numberOfEpisodes = 62),
                progress = EpisodesProgressUiModel.Completed
            )
        }

        // assert
        rule.onNodeWithText(string(R.string.episodes_title)).assertIsDisplayed()
        rule.onNodeWithText("5 seasons • 62 episodes").assertIsDisplayed()
        rule.onNodeWithText("100%").assertIsDisplayed()
        rule.onNode(
            hasProgressBarRangeInfo(ProgressBarRangeInfo(1f, 0f..1f)),
            useUnmergedTree = true
        ).assertExists()
    }

    @Test
    fun `should display premiere date and not be clickable when show is not released`() {
        // arrange
        val onClick: () -> Unit = mockk(relaxed = true)
        rule.setContent {
            UiEpisodesCard(
                model = fakeEpisodesSummary().copy(
                    isReleased = false,
                    releaseDate = "Mar 12, 2027"
                ),
                progress = EpisodesProgressUiModel.SignedOut,
                onClick = onClick
            )
        }

        // act
        rule.onNodeWithText(string(R.string.episodes_title)).performClick()

        // assert
        rule.onNodeWithText("Premieres Mar 12, 2027").assertIsDisplayed()
        rule.onNodeWithText(string(R.string.episodes_title)).assertIsNotEnabled()
        rule.onNodeWithText(string(R.string.episodes_sign_in_hint)).assertDoesNotExist()
        verify(exactly = 0) { onClick() }
    }

    @Test
    fun `should not display texts when progress is Loading`() {
        // act
        rule.setContent {
            UiEpisodesCard(
                model = fakeEpisodesSummary(),
                progress = EpisodesProgressUiModel.Loading
            )
        }

        // assert
        rule.onNodeWithText(string(R.string.episodes_title)).assertDoesNotExist()
    }

    private fun string(@StringRes id: Int) = RuntimeEnvironment.getApplication().getString(id)
}
