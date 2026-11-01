package br.dev.singular.overview.presentation.ui.components

import androidx.hilt.lifecycle.viewmodel.HiltViewModelFactory
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.dev.singular.overview.domain.model.Prediction
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.prediction.IGetPredictionUseCase
import br.dev.singular.overview.domain.usecase.user.IUserSessionUseCase
import br.dev.singular.overview.presentation.createUserMock
import br.dev.singular.overview.presentation.ui.components.episodes.EpisodesProgressViewModel
import br.dev.singular.overview.presentation.ui.components.prediction.AiPredictionViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher

private const val HILT_VIEW_MODEL_FACTORY = "androidx.hilt.lifecycle.viewmodel.HiltViewModelFactory"

/**
 * Makes the default `hiltViewModel` of the media details hosts resolve an
 * [AiPredictionViewModel] and an [EpisodesProgressViewModel] in tests that run without a
 * Hilt component, with a signed-in user.
 * Call `unmockkAll` after the test to restore the real factory.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun mockHiltViewModelFactory() {
    val predictionUseCase = mockk<IGetPredictionUseCase>()
    coEvery { predictionUseCase(any()) } returns UseCaseState.Success(
        data = Prediction.Unavailable
    )

    val sessionUseCase = mockk<IUserSessionUseCase>()
    every { sessionUseCase.observe() } returns flowOf(createUserMock())

    mockkStatic(HILT_VIEW_MODEL_FACTORY)
    every { HiltViewModelFactory(any(), any()) } returns viewModelFactory {
        initializer {
            AiPredictionViewModel(
                dispatcher = UnconfinedTestDispatcher(),
                predictionUseCase = predictionUseCase,
                sessionUseCase = sessionUseCase
            )
        }
        initializer {
            EpisodesProgressViewModel(
                dispatcher = UnconfinedTestDispatcher(),
                sessionUseCase = sessionUseCase
            )
        }
    }
}
