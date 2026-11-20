package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.TvShowDetails
import br.dev.singular.overview.domain.repository.GetById
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.createTvShowDetailsMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetTvShowDetailsByIdUseCaseTest {

    private lateinit var sut: IGetTvShowDetailsByIdUseCase

    private lateinit var getter: GetById<TvShowDetails>

    @Before
    fun setup() {
        getter = mockk()
        sut = GetTvShowDetailsByIdUseCase(getter)
    }

    @Test
    fun `invoke should return success with tv show details`() = runTest {
        // arrange
        val tvShow = createTvShowDetailsMock()
        coEvery { getter.getById(1L) } returns tvShow

        // act
        val result = sut.invoke(1L)

        // assert
        coVerify(exactly = 1) { getter.getById(1L) }
        assertEquals(UseCaseState.Success(tvShow), result)
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
