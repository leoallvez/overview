package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.MovieDetails
import br.dev.singular.overview.domain.repository.GetById
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.createMovieDetailsMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetMovieDetailsByIdUseCaseTest {

    private lateinit var sut: IGetMovieDetailsByIdUseCase

    private lateinit var getter: GetById<MovieDetails>

    @Before
    fun setup() {
        getter = mockk()
        sut = GetMovieDetailsByIdUseCase(getter)
    }

    @Test
    fun `invoke should return success with movie details`() = runTest {
        // arrange
        val movie = createMovieDetailsMock()
        coEvery { getter.getById(1L) } returns movie

        // act
        val result = sut.invoke(1L)

        // assert
        coVerify(exactly = 1) { getter.getById(1L) }
        assertEquals(UseCaseState.Success(movie), result)
    }

    @Test
    fun `invoke should return success with null`() = runTest {
        // arrange
        coEvery { getter.getById(1L) } returns null

        // act
        val result = sut.invoke(1L)

        // assert
        coVerify(exactly = 1) { getter.getById(1L) }
        assertEquals(UseCaseState.Success(null), result)
    }

    @Test
    fun `invoke should return Failure when getter throws exception`() = runTest {
        // Arrange
        val expectedException = RuntimeException("Getter failed")
        coEvery { getter.getById(any()) } throws expectedException

        // Act
        val result = sut.invoke(0)

        // Assert
        assertTrue(result is UseCaseState.Failure)
        val failure = result as UseCaseState.Failure
        assertEquals(expectedException, (failure.type as FailType.Exception).throwable)
    }
}
