package br.dev.singular.overview.domain.usecase.genre

import br.dev.singular.overview.domain.model.Genre
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.MediaTypeGenres
import br.dev.singular.overview.domain.repository.GetByParam
import br.dev.singular.overview.domain.repository.Update
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SaveGenreUseCaseTest {

    private lateinit var sut: ISaveGenreUseCase
    private lateinit var setter: Update<MediaTypeGenres>
    private lateinit var getter: GetByParam<List<Genre>, MediaType>

    @Before
    fun setup() {
        setter = mockk(relaxed = true)
        getter = mockk()
        sut = SaveGenreUseCase(setter, getter)
    }

    @Test
    fun `invoke should fetch and save genres for both TV and Movie types`() = runTest {
        // Arrange
        val tvGenres = listOf(Genre(id = 1, name = "Drama"))
        val movieGenres = listOf(Genre(id = 2, name = "Action"))

        coEvery { getter.getByParam(MediaType.TV) } returns tvGenres
        coEvery { getter.getByParam(MediaType.MOVIE) } returns movieGenres

        coEvery { setter.update(any()) } returns Unit

        // Act
        val result = sut.invoke()

        // Assert
        coVerifyOrder {
            getter.getByParam(MediaType.TV)
            setter.update(MediaTypeGenres(MediaType.TV, tvGenres))

            getter.getByParam(MediaType.MOVIE)
            setter.update(MediaTypeGenres(MediaType.MOVIE, movieGenres))
        }

        assertTrue(result is UseCaseState.Success)

        confirmVerified(getter, setter)
    }

    @Test
    fun `invoke should return failure when getter throws exception`() = runBlocking {
        // arrange
        val expectedException = Exception("Getter failed")
        coEvery { getter.getByParam(any()) } throws expectedException

        // act
        val result = sut.invoke()

        // assert
        assertTrue(result is UseCaseState.Failure)
        val failure = result as UseCaseState.Failure
        assertEquals(
            expectedException,
            (failure.type as FailType.Exception).throwable
        )
    }

    @Test
    fun `invoke should return failure when setter throws exception`() = runBlocking {
        // arrange
        val expectedException = Exception("Setter failed")
        coEvery { getter.getByParam(any()) } returns emptyList()
        coEvery { setter.update(any()) } throws expectedException

        // act
        val result = sut.invoke()

        // assert
        assertTrue(result is UseCaseState.Failure)
        val failure = result as UseCaseState.Failure
        assertEquals(
            expectedException,
            (failure.type as FailType.Exception).throwable
        )
    }
}
