package br.dev.singular.overview.presentation.ui.components.episodes

import androidx.annotation.StringRes
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.EpisodesProgressUiModel
import br.dev.singular.overview.presentation.ui.utils.fakeEpisodesSummary
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiEpisodesCardHostTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `should display skeleton while progress is Loading`() {
        // arrange
        val viewModel = FakeEpisodesProgressViewModel(EpisodesProgressUiModel.Loading)

        // act
        setHost(viewModel)

        // assert
        rule.onNodeWithText(string(R.string.episodes_title)).assertDoesNotExist()
    }

    @Test
    fun `should display show summary when user has not started the show`() {
        // arrange
        val viewModel = FakeEpisodesProgressViewModel(EpisodesProgressUiModel.NotStarted)

        // act
        setHost(viewModel)

        // assert
        rule.onNodeWithText("5 seasons • 62 episodes").assertIsDisplayed()
        rule.onNodeWithText(string(R.string.episodes_sign_in_hint)).assertDoesNotExist()
    }

    @Test
    fun `should display sign in hint when user is signed out`() {
        // arrange
        val viewModel = FakeEpisodesProgressViewModel(EpisodesProgressUiModel.SignedOut)

        // act
        setHost(viewModel)

        // assert
        rule.onNodeWithText(string(R.string.episodes_sign_in_hint)).assertIsDisplayed()
    }

    @Test
    fun `should update the card when progress changes`() {
        // arrange
        val viewModel = FakeEpisodesProgressViewModel(EpisodesProgressUiModel.Loading)
        setHost(viewModel)

        // act
        viewModel.emit(EpisodesProgressUiModel.SignedOut)
        rule.waitForIdle()

        // assert
        rule.onNodeWithText(string(R.string.episodes_sign_in_hint)).assertIsDisplayed()
    }

    @Test
    fun `should call onClick when card is clicked`() {
        // arrange
        val onClick: () -> Unit = mockk(relaxed = true)
        val viewModel = FakeEpisodesProgressViewModel(EpisodesProgressUiModel.NotStarted)
        setHost(viewModel, onClick = onClick)

        // act
        rule.onNodeWithText(string(R.string.episodes_title)).performClick()

        // assert
        verify(exactly = 1) { onClick() }
    }

    private fun setHost(
        viewModel: IEpisodesProgressViewModel,
        onClick: () -> Unit = {}
    ) {
        rule.setContent {
            UiEpisodesCardHost(
                model = fakeEpisodesSummary(),
                onClick = onClick,
                viewModel = viewModel
            )
        }
    }

    private fun string(@StringRes id: Int) = RuntimeEnvironment.getApplication().getString(id)

    private class FakeEpisodesProgressViewModel(
        initial: EpisodesProgressUiModel
    ) : IEpisodesProgressViewModel {

        private val _uiState = MutableStateFlow(initial)
        override val uiState: StateFlow<EpisodesProgressUiModel> = _uiState

        fun emit(model: EpisodesProgressUiModel) {
            _uiState.value = model
        }
    }
}
