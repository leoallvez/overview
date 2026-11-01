package br.dev.singular.overview.domain.usecase.prediction

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.Prediction
import br.dev.singular.overview.domain.model.PredictionInput
import br.dev.singular.overview.domain.repository.GetByParam
import br.dev.singular.overview.domain.repository.Observe
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.createMediaMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetPredictionUseCaseTest {

    private lateinit var sut: IGetPredictionUseCase

    private lateinit var getter: GetByParam<Prediction, PredictionInput>

    private lateinit var favorites: Observe<List<Media>>

    private val predictionInput = PredictionInput(
        id = 1L,
        type = MediaType.MOVIE,
        title = "Original Title",
        genres = listOf("Action")
    )

    private val favorite = createMediaMock().copy(id = 2L, genres = listOf("Drama"))

    @Before
    fun setup() {
        getter = mockk()
        favorites = mockk()
        every { favorites.observe() } returns flowOf(listOf(favorite))
        sut = GetPredictionUseCase(getter, favorites)
    }

    @Test
    fun `invoke should return success with available prediction`() = runTest {
        // arrange
        val prediction = Prediction.Available(affinityPercentage = 90)
        coEvery { getter.getByParam(any()) } returns prediction

        // act
        val result = sut.invoke(predictionInput)

        // assert
        coVerify(exactly = 1) { getter.getByParam(any()) }
        assertEquals(UseCaseState.Success(prediction), result)
    }

    @Test
    fun `invoke should return success with error prediction`() = runTest {
        // arrange
        coEvery { getter.getByParam(any()) } returns Prediction.Error

        // act
        val result = sut.invoke(predictionInput)

        // assert
        coVerify(exactly = 1) { getter.getByParam(any()) }
        assertEquals(UseCaseState.Success(Prediction.Error), result)
    }

    @Test
    fun `invoke should return unavailable without fetching when there are no favorites`() =
        runTest {
            // arrange
            every { favorites.observe() } returns flowOf(emptyList())

            // act
            val result = sut.invoke(predictionInput)

            // assert
            coVerify(exactly = 0) { getter.getByParam(any()) }
            assertEquals(UseCaseState.Success(Prediction.Unavailable), result)
        }

    @Test
    fun `invoke should return unavailable when the only favorite is the media itself`() =
        runTest {
            // arrange
            val itself = createMediaMock().copy(id = predictionInput.id)
            every { favorites.observe() } returns flowOf(listOf(itself))

            // act
            val result = sut.invoke(predictionInput)

            // assert
            coVerify(exactly = 0) { getter.getByParam(any()) }
            assertEquals(UseCaseState.Success(Prediction.Unavailable), result)
        }

    @Test
    fun `invoke should keep a favorite with the same id but another type`() = runTest {
        // arrange
        val tvShow = createMediaMock(type = MediaType.TV).copy(id = predictionInput.id)
        every { favorites.observe() } returns flowOf(listOf(tvShow))
        val input = slot<PredictionInput>()
        coEvery { getter.getByParam(capture(input)) } returns Prediction.Error

        // act
        sut.invoke(predictionInput)

        // assert
        assertEquals(listOf(tvShow), input.captured.favorites)
    }

    @Test
    fun `invoke should send at most the ten first favorites`() = runTest {
        // arrange
        val list = (2L..13L).map { createMediaMock().copy(id = it) }
        every { favorites.observe() } returns flowOf(list)
        val input = slot<PredictionInput>()
        coEvery { getter.getByParam(capture(input)) } returns Prediction.Error

        // act
        sut.invoke(predictionInput)

        // assert
        assertEquals(list.take(10), input.captured.favorites)
    }

    @Test
    fun `invoke should count the genres of every favorite`() = runTest {
        // arrange
        val list = (2L..13L).map { createMediaMock().copy(id = it, genres = listOf("Drama")) } +
            createMediaMock().copy(id = 14L, genres = listOf("Drama", "Crime"))
        every { favorites.observe() } returns flowOf(list)
        val input = slot<PredictionInput>()
        coEvery { getter.getByParam(capture(input)) } returns Prediction.Error

        // act
        sut.invoke(predictionInput)

        // assert
        assertEquals(mapOf("Drama" to 13, "Crime" to 1), input.captured.favoriteGenres)
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
