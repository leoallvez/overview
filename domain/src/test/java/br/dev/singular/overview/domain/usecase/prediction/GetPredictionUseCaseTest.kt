package br.dev.singular.overview.domain.usecase.prediction

import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.Prediction
import br.dev.singular.overview.domain.model.PredictionInput
import br.dev.singular.overview.domain.repository.GetByParam
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetPredictionUseCaseTest {

    private lateinit var sut: IGetPredictionUseCase

    private lateinit var getter: GetByParam<Prediction, PredictionInput>

    private val predictionInput = PredictionInput(
        id = 1L,
        type = MediaType.MOVIE,
        title = "Original Title",
        genres = listOf("Action")
    )

    @Before
    fun setup() {
        getter = mockk()
        sut = GetPredictionUseCase(getter)
    }

    @Test
    fun `invoke should return success with available prediction`() = runTest {
        // arrange
        val prediction = Prediction.Available(affinityPercentage = 90)
        coEvery { getter.getByParam(predictionInput) } returns prediction

        // act
        val result = sut.invoke(predictionInput)

        // assert
        coVerify(exactly = 1) { getter.getByParam(predictionInput) }
        assertEquals(UseCaseState.Success(prediction), result)
    }

    @Test
    fun `invoke should return success with unavailable prediction`() = runTest {
        // arrange
        coEvery { getter.getByParam(predictionInput) } returns Prediction.Unavailable

        // act
        val result = sut.invoke(predictionInput)

        // assert
        coVerify(exactly = 1) { getter.getByParam(predictionInput) }
        assertEquals(UseCaseState.Success(Prediction.Unavailable), result)
    }

    @Test
    fun `invoke should return success with error prediction`() = runTest {
        // arrange
        coEvery { getter.getByParam(predictionInput) } returns Prediction.Error

        // act
        val result = sut.invoke(predictionInput)

        // assert
        coVerify(exactly = 1) { getter.getByParam(predictionInput) }
        assertEquals(UseCaseState.Success(Prediction.Error), result)
    }

    @Test
    fun `invoke should return failure when getter throws exception`() = runTest {
        // arrange
        val expectedException = RuntimeException("Getter failed")
        coEvery { getter.getByParam(any()) } throws expectedException

        // act
        val result = sut.invoke(predictionInput)

        // assert
        assertTrue(result is UseCaseState.Failure)
        val failure = result as UseCaseState.Failure
        assertEquals(expectedException, (failure.type as FailType.Exception).throwable)
    }
}
