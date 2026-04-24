package br.dev.singular.overview.domain.usecase.genre

import br.dev.singular.overview.domain.model.Genre
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.repository.GetByParam
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
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
    fun `invoke should return sorted genre list`() = runBlocking {
        // arrange
        val genres = listOf(
            Genre(2, "B"),
            Genre(1, "A"),
            Genre(3, "C")
        )
        val sortedGenres = genres.sortedBy { it.name }
        coEvery { getter.getByParam(any()) } returns genres

        // act
        val result = sut.invoke(MediaType.MOVIE)

        // assert
        coVerify(exactly = 1) { getter.getByParam(MediaType.MOVIE) }
        assertEquals(sortedGenres, result)
    }

    @Test
    fun `invoke should return empty list when getter throws exception`() = runBlocking {
        // arrange
        val expectedException = Exception("Getter failed")
        coEvery { getter.getByParam(any()) } throws expectedException

        // act
        val result = sut.invoke(MediaType.TV)

        // assert
        coVerify(exactly = 1) { getter.getByParam(MediaType.TV) }
        assertEquals(emptyList<Genre>(), result)
    }
}
