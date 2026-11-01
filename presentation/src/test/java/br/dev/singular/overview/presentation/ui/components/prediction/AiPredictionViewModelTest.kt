package br.dev.singular.overview.presentation.ui.components.prediction

import br.dev.singular.overview.domain.model.Prediction
import br.dev.singular.overview.domain.model.PredictionInput
import br.dev.singular.overview.domain.model.User
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.prediction.IGetPredictionUseCase
import br.dev.singular.overview.domain.usecase.user.IUserSessionUseCase
import br.dev.singular.overview.presentation.createMovieDetailsMock
import br.dev.singular.overview.presentation.createTvShowDetailsMock
import br.dev.singular.overview.presentation.createUserMock
import br.dev.singular.overview.presentation.model.PredictionUiModel
import br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi.toUi
import br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain.toDomain
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class AiPredictionViewModelTest {

    private val predictionUseCase: IGetPredictionUseCase = mockk()
    private val sessionUseCase: IUserSessionUseCase = mockk()
    private val session = MutableStateFlow<User?>(createUserMock())
    private val movie = createMovieDetailsMock().toUi(isLiked = false)
    private val tvShow = createTvShowDetailsMock().toUi(isLiked = false)
    private lateinit var sut: IAiPredictionViewModel

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
        loading shouldBeEqualTo PredictionUiModel.Loading
        sut.uiState.value shouldBeEqualTo PredictionUiModel.Idle
    }

    @Test
    fun `uiState should emit LoginRequired when observing the session fails`() {
        // arrange
        every { sessionUseCase.observe() } returns flow { throw IllegalStateException() }

        // act
        sut = createSut()

        // assert
        sut.uiState.value shouldBeEqualTo PredictionUiModel.LoginRequired
    }

    @Test
    fun `uiState should start as Idle when the user is signed in`() {
        // assert
        sut.uiState.value shouldBeEqualTo PredictionUiModel.Idle
    }

    @Test
    fun `uiState should start as LoginRequired when the user is signed out`() {
        // arrange
        session.value = null

        // act
        sut = createSut()

        // assert
        sut.uiState.value shouldBeEqualTo PredictionUiModel.LoginRequired
    }

    @Test
    fun `uiState should emit LoginRequired when the user signs out`() = runTest {
        // arrange
        coEvery { predictionUseCase(any()) } returns UseCaseState.Success(
            Prediction.Available(
                affinityPercentage = 90
            )
        )
        sut.onPredict(model = movie)

        // act
        session.value = null

        // assert
        sut.uiState.value shouldBeEqualTo PredictionUiModel.LoginRequired
    }

    @Test
    fun `uiState should emit Idle when the user signs in`() {
        // arrange
        session.value = null
        sut = createSut()

        // act
        session.value = createUserMock()

        // assert
        sut.uiState.value shouldBeEqualTo PredictionUiModel.Idle
    }

    @Test
    fun `uiState should keep the prediction when the session emits the same status`() = runTest {
        // arrange
        coEvery { predictionUseCase(any()) } returns UseCaseState.Success(
            Prediction.Available(
                affinityPercentage = 90
            )
        )
        sut.onPredict(model = movie)

        // act
        session.value = createUserMock().copy(name = "Another Name")

        // assert
        sut.uiState.value shouldBeEqualTo PredictionUiModel.Predicted(affinityPercentage = 90)
    }

    @Test
    fun `onPredict should emit Loading while the prediction is requested`() = runTest {
        // arrange
        sut = createSut(StandardTestDispatcher(testScheduler))
        coEvery { predictionUseCase(input = any()) } coAnswers {
            delay(1.seconds)
            UseCaseState.Success(Prediction.Unavailable)
        }

        // act
        sut.onPredict(movie)
        runCurrent()
        val loading = sut.uiState.value
        advanceUntilIdle()

        // assert
        loading shouldBeEqualTo PredictionUiModel.Loading
        sut.uiState.value shouldBeEqualTo PredictionUiModel.Unavailable
    }

    @Test
    fun `onPredict should request the prediction with the movie mapped to domain`() = runTest {
        // arrange
        val media = slot<PredictionInput>()
        val prediction = Prediction.Available(affinityPercentage = 90)
        coEvery { predictionUseCase(input = capture(media)) } returns UseCaseState.Success(
            prediction
        )

        // act
        sut.onPredict(model = movie)

        // assert
        sut.uiState.value shouldBeEqualTo PredictionUiModel.Predicted(affinityPercentage = 90)
        media.captured shouldBeEqualTo movie.toDomain()
        coVerify(exactly = 1) { predictionUseCase(any()) }
    }

    @Test
    fun `onPredict should request the prediction with the tv show mapped to domain`() = runTest {
        // arrange
        val media = slot<PredictionInput>()
        val prediction = Prediction.Available(affinityPercentage = 40)
        coEvery { predictionUseCase(input = capture(media)) } returns UseCaseState.Success(
            prediction
        )

        // act
        sut.onPredict(model = tvShow)

        // assert
        sut.uiState.value shouldBeEqualTo PredictionUiModel.Predicted(affinityPercentage = 40)
        media.captured shouldBeEqualTo tvShow.toDomain()
        coVerify(exactly = 1) { predictionUseCase(input = any()) }
    }

    @Test
    fun `onPredict should emit Unavailable when the prediction is unavailable`() = runTest {
        // arrange
        coEvery { predictionUseCase(any()) } returns UseCaseState.Success(Prediction.Unavailable)

        // act
        sut.onPredict(model = movie)

        // assert
        sut.uiState.value shouldBeEqualTo PredictionUiModel.Unavailable
    }

    @Test
    fun `onPredict should emit Error when the prediction is an error`() = runTest {
        // arrange
        coEvery { predictionUseCase(any()) } returns UseCaseState.Success(Prediction.Error)

        // act
        sut.onPredict(model = movie)

        // assert
        sut.uiState.value shouldBeEqualTo PredictionUiModel.Error
    }

    @Test
    fun `onPredict should emit Error when the prediction use case fails`() = runTest {
        // arrange
        coEvery { predictionUseCase(any()) } returns UseCaseState.Failure(FailType.NothingFound)

        // act
        sut.onPredict(model = movie)

        // assert
        sut.uiState.value shouldBeEqualTo PredictionUiModel.Error
    }

    private fun createSut(
        dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
    ) = AiPredictionViewModel(
        dispatcher = dispatcher,
        predictionUseCase = predictionUseCase,
        sessionUseCase = sessionUseCase
    )
}
