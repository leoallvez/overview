package br.dev.singular.overview.presentation.ui.components.episodes

import br.dev.singular.overview.domain.model.User
import br.dev.singular.overview.domain.usecase.user.IUserSessionUseCase
import br.dev.singular.overview.presentation.createUserMock
import br.dev.singular.overview.presentation.model.EpisodesProgressUiModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EpisodesProgressViewModelTest {

    private val sessionUseCase: IUserSessionUseCase = mockk()
    private val session = MutableStateFlow<User?>(createUserMock())
    private lateinit var sut: IEpisodesProgressViewModel

    @Before
    fun setup() {
        every { sessionUseCase.observe() } returns session
        sut = createSut()
    }

    @Test
    fun `uiState should be Loading until the session is resolved`() = runTest {
        // arrange
        sut = createSut(StandardTestDispatcher(testScheduler))

        // act
        val loading = sut.uiState.value
        advanceUntilIdle()

        // assert
        loading shouldBeEqualTo EpisodesProgressUiModel.Loading
        sut.uiState.value shouldBeEqualTo EpisodesProgressUiModel.NotStarted
    }

    @Test
    fun `uiState should start as NotStarted when the user is signed in`() {
        // assert
        sut.uiState.value shouldBeEqualTo EpisodesProgressUiModel.NotStarted
    }

    @Test
    fun `uiState should start as SignedOut when the user is signed out`() {
        // arrange
        session.value = null

        // act
        sut = createSut()

        // assert
        sut.uiState.value shouldBeEqualTo EpisodesProgressUiModel.SignedOut
    }

    @Test
    fun `uiState should emit SignedOut when observing the session fails`() {
        // arrange
        every { sessionUseCase.observe() } returns flow { throw IllegalStateException() }

        // act
        sut = createSut()

        // assert
        sut.uiState.value shouldBeEqualTo EpisodesProgressUiModel.SignedOut
    }

    @Test
    fun `uiState should emit SignedOut when the user signs out`() {
        // act
        session.value = null

        // assert
        sut.uiState.value shouldBeEqualTo EpisodesProgressUiModel.SignedOut
    }

    @Test
    fun `uiState should emit NotStarted when the user signs in`() {
        // arrange
        session.value = null
        sut = createSut()

        // act
        session.value = createUserMock()

        // assert
        sut.uiState.value shouldBeEqualTo EpisodesProgressUiModel.NotStarted
    }

    private fun createSut(
        dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
    ) = EpisodesProgressViewModel(
        dispatcher = dispatcher,
        sessionUseCase = sessionUseCase
    )
}
