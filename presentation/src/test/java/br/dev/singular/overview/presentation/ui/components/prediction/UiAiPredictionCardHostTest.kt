package br.dev.singular.overview.presentation.ui.components.prediction

import androidx.annotation.StringRes
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaDetailsUiModel
import br.dev.singular.overview.presentation.model.PredictionUiModel
import br.dev.singular.overview.presentation.ui.utils.fakeMovieDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiAiPredictionCardHostTest {

    @get:Rule
    val rule = createComposeRule()

    private lateinit var media: MediaDetailsUiModel

    @Test
    fun `should display idle state when view model starts idle`() {
        // arrange
        val viewModel = FakeAiPredictionViewModel(PredictionUiModel.Idle)

        // act
        setHost(viewModel)

        // assert
        rule.onNodeWithText(string(R.string.taste_prediction_idle)).assertIsDisplayed()
        rule.onNodeWithText(string(R.string.taste_prediction_action)).assertIsDisplayed()
    }

    @Test
    fun `should request prediction for the given media when action button is clicked`() {
        // arrange
        val viewModel = FakeAiPredictionViewModel(PredictionUiModel.Idle)
        setHost(viewModel)

        // act
        rule.onNodeWithText(string(R.string.taste_prediction_action)).performClick()

        // assert
        assertEquals(listOf<MediaDetailsUiModel>(media), viewModel.requestedMedias)
    }

    @Test
    fun `should request prediction for the given media when try again is clicked`() {
        // arrange
        val viewModel = FakeAiPredictionViewModel(PredictionUiModel.Error)
        setHost(viewModel)

        // act
        rule.onNodeWithText(string(R.string.btn_try_again)).performClick()

        // assert
        assertEquals(listOf<MediaDetailsUiModel>(media), viewModel.requestedMedias)
    }

    @Test
    fun `should trigger onPredict without try again when action button is clicked`() {
        // arrange
        val clicks = mutableListOf<Boolean>()
        val viewModel = FakeAiPredictionViewModel(PredictionUiModel.Idle)
        setHost(viewModel, onPredict = { clicks += it })

        // act
        rule.onNodeWithText(string(R.string.taste_prediction_action)).performClick()

        // assert
        assertEquals(listOf(false), clicks)
        assertEquals(listOf<MediaDetailsUiModel>(media), viewModel.requestedMedias)
    }

    @Test
    fun `should trigger onPredict as try again when try again is clicked`() {
        // arrange
        val clicks = mutableListOf<Boolean>()
        val viewModel = FakeAiPredictionViewModel(PredictionUiModel.Error)
        setHost(viewModel, onPredict = { clicks += it })

        // act
        rule.onNodeWithText(string(R.string.btn_try_again)).performClick()

        // assert
        assertEquals(listOf(true), clicks)
    }

    @Test
    fun `should not request prediction when nothing is clicked`() {
        // arrange
        val viewModel = FakeAiPredictionViewModel(PredictionUiModel.Idle)

        // act
        setHost(viewModel)
        rule.waitForIdle()

        // assert
        assertTrue(viewModel.requestedMedias.isEmpty())
    }

    @Test
    fun `should replace idle content with skeleton when view model emits Loading`() {
        // arrange
        val viewModel = FakeAiPredictionViewModel(PredictionUiModel.Idle)
        setHost(viewModel)

        // act
        viewModel.emit(PredictionUiModel.Loading)
        rule.waitForIdle()

        // assert
        rule.onNodeWithText(string(R.string.taste_prediction_title)).assertDoesNotExist()
        rule.onNodeWithText(string(R.string.taste_prediction_action)).assertDoesNotExist()
    }

    @Test
    fun `should display result when view model emits Predicted`() {
        // arrange
        val viewModel = FakeAiPredictionViewModel(PredictionUiModel.Loading)
        setHost(viewModel)

        // act
        viewModel.emit(PredictionUiModel.Predicted(affinityPercentage = 90))
        rule.waitForIdle()

        // assert
        rule.onNodeWithText("90%").assertIsDisplayed()
        rule.onNodeWithText(string(R.string.taste_prediction_high)).assertIsDisplayed()
    }

    @Test
    fun `should update result when view model emits another Predicted`() {
        // arrange
        val viewModel = FakeAiPredictionViewModel(
            PredictionUiModel.Predicted(affinityPercentage = 90)
        )
        setHost(viewModel)

        // act
        viewModel.emit(PredictionUiModel.Predicted(affinityPercentage = 10))
        rule.waitForIdle()

        // assert
        rule.onNodeWithText("10%").assertIsDisplayed()
        rule.onNodeWithText("90%").assertDoesNotExist()
    }

    @Test
    fun `should display error state when view model emits Error`() {
        // arrange
        val viewModel = FakeAiPredictionViewModel(PredictionUiModel.Loading)
        setHost(viewModel)

        // act
        viewModel.emit(PredictionUiModel.Error)
        rule.waitForIdle()

        // assert
        rule.onNodeWithText(string(R.string.taste_prediction_error_title)).assertIsDisplayed()
        rule.onNodeWithText(string(R.string.btn_try_again)).assertIsDisplayed()
    }

    @Test
    fun `should invoke onLogin without requesting prediction when login is clicked`() {
        // arrange
        var logins = 0
        val viewModel = FakeAiPredictionViewModel(PredictionUiModel.LoginRequired)
        setHost(viewModel, onLogin = { logins++ })

        // act
        rule.onNodeWithText(string(R.string.login)).performClick()

        // assert
        assertEquals(1, logins)
        assertEquals(emptyList<MediaDetailsUiModel>(), viewModel.requestedMedias)
    }

    private fun setHost(
        viewModel: IAiPredictionViewModel,
        onPredict: (isTryAgain: Boolean) -> Unit = {},
        onLogin: () -> Unit = {}
    ) {
        rule.setContent {
            val model = fakeMovieDetails()
            media = model
            UiAiPredictionCardHost(
                media = model,
                onPredict = onPredict,
                onLogin = onLogin,
                viewModel = viewModel
            )
        }
    }

    private fun string(@StringRes id: Int) = RuntimeEnvironment.getApplication().getString(id)

    private class FakeAiPredictionViewModel(
        initial: PredictionUiModel
    ) : IAiPredictionViewModel {

        private val _uiState = MutableStateFlow(initial)
        override val uiState: StateFlow<PredictionUiModel> = _uiState

        val requestedMedias = mutableListOf<MediaDetailsUiModel>()

        override fun onPredict(model: MediaDetailsUiModel) {
            requestedMedias += model
        }

        fun emit(model: PredictionUiModel) {
            _uiState.value = model
        }
    }
}
