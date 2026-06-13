package br.dev.singular.overview.domain.usecase.genre

import br.dev.singular.overview.domain.model.Genre
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.repository.GetByParam
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FetchGenresByTypeUseCaseTest {

    private lateinit var sut: IFetchGenresByTypeUseCase
    private lateinit var getter: GetByParam<List<Genre>, MediaType>

    @Before
    fun setup() {
        getter = mockk()
        sut = FetchGenresByTypeUseCase(getter)
    }

    @Test
    fun `invoke should return sorted genre list by name`() = runTest {
        // arrange
        val genres = listOf(
            Genre(2, "Comedy"),
            Genre(1, "Action"),
            Genre(3, "Drama")
        )
        // Expected order: Action (1), Comedy (2), Drama (3)
        val expected = listOf(genres[1], genres[0], genres[2])
        
        coEvery { getter.getByParam(MediaType.MOVIE) } returns genres

        // act
        val result = sut.invoke(MediaType.MOVIE)

        // assert
        coVerify(exactly = 1) { getter.getByParam(MediaType.MOVIE) }
        assertEquals(expected, result)
        assertEquals("Action", result[0].name)
        assertEquals("Comedy", result[1].name)
        assertEquals("Drama", result[2].name)
    }

    @Test
    fun `invoke should return empty list when getter returns null`() = runTest {
        // arrange
        coEvery { getter.getByParam(any()) } throws NullPointerException()

        // act
        val result = sut.invoke(MediaType.TV)

        // assert
        assertEquals(emptyList<Genre>(), result)
    }

    @Test
    fun `invoke should return empty list when getter throws exception`() = runTest {
        // arrange
        val expectedException = RuntimeException("Network Error")
        coEvery { getter.getByParam(any()) } throws expectedException

        // act
        val result = sut.invoke(MediaType.TV)

        // assert
        coVerify(exactly = 1) { getter.getByParam(MediaType.TV) }
        assertEquals(emptyList<Genre>(), result)
    }
}
