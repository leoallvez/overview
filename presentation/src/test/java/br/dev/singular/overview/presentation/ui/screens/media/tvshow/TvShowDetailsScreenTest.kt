package br.dev.singular.overview.presentation.ui.screens.media.tvshow

import androidx.annotation.StringRes
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.ui.components.prediction.mockAiPredictionViewModelFactory
import br.dev.singular.overview.presentation.ui.screens.media.tvshow.interaction.rememberTvShowDetailsActions
import br.dev.singular.overview.presentation.ui.utils.fakeTvShowDetails
import io.mockk.justRun
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class TvShowDetailsScreenTest {

    @get:Rule
    val rule = createComposeRule()

    private val predictionTitle = string(R.string.taste_prediction_title)
    private val predictionAction = string(R.string.taste_prediction_action)
    private val synopsisTitle = string(R.string.synopsis)

    @Before
    fun setup() {
        mockAiPredictionViewModelFactory()
    }

    @Test
    fun `TvShowDetailsScreen should display the AI prediction card by default`() {
        // arrange
        rule.setContent {
            TvShowDetailsScreen(
                tvShowId = 1L,
                uiState = UiState.Success(data = fakeTvShowDetails()),
                actions = rememberTvShowDetailsActions()
            )
        }

        // act
        scrollTo(predictionAction)

        // assert
        rule.onNodeWithText(predictionTitle).assertIsDisplayed()
        rule.onNodeWithText(predictionAction).assertIsDisplayed()
    }

    @Test
    fun `TvShowDetailsScreen should NOT display the AI prediction card when showAiPrediction is false`() {
        // arrange
        rule.setContent {
            TvShowDetailsScreen(
                tvShowId = 1L,
                uiState = UiState.Success(data = fakeTvShowDetails()),
                showAiPrediction = false,
                actions = rememberTvShowDetailsActions()
            )
        }

        // act
        scrollTo(synopsisTitle)

        // assert
        rule.onNodeWithText(synopsisTitle).assertIsDisplayed()
        rule.onNodeWithText(predictionTitle).assertDoesNotExist()
        rule.onNodeWithText(predictionAction).assertDoesNotExist()
    }

    @Test
    fun `TvShowDetailsScreen should display the AI prediction card when showAiPrediction is true`() {
        // arrange
        rule.setContent {
            TvShowDetailsScreen(
                tvShowId = 1L,
                uiState = UiState.Success(data = fakeTvShowDetails()),
                showAiPrediction = true,
                actions = rememberTvShowDetailsActions()
            )
        }

        // act
        scrollTo(predictionAction)

        // assert
        rule.onNodeWithText(predictionTitle).assertIsDisplayed()
        rule.onNodeWithText(predictionAction).assertIsDisplayed()
    }

    @Test
    fun `TvShowDetailsScreen should log click when the AI prediction is requested`() {
        // arrange
        mockkObject(TagManager)
        justRun { TagManager.logClick(any(), any(), any()) }
        var mediaId = 0L
        var tagPath = ""
        rule.setContent {
            val model = fakeTvShowDetails()
            val actions = rememberTvShowDetailsActions()
            mediaId = model.metadata.id
            tagPath = actions.tagPath
            TvShowDetailsScreen(
                tvShowId = mediaId,
                uiState = UiState.Success(data = model),
                showAiPrediction = true,
                actions = actions
            )
        }
        scrollTo(predictionAction)

        // act
        rule.onNodeWithText(predictionAction).performClick()

        // assert
        verify(exactly = 1) { TagManager.logClick(tagPath, "ai-prediction", mediaId) }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    private fun scrollTo(text: String) {
        rule.onAllNodes(hasScrollToNodeAction()).onFirst().performScrollToNode(hasText(text))
    }

    private fun string(@StringRes id: Int) = RuntimeEnvironment.getApplication().getString(id)
}
