package br.dev.singular.overview.data.local.source

import br.dev.singular.overview.data.local.database.dao.GenreDao
import br.dev.singular.overview.data.model.GenreDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.MediaTypeGenreDataModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GenreLocalDataSourceTest {

    private val dao: GenreDao = mockk(relaxed = true)
    private val sut = GenreLocalDataSource(dao)

    @Test
    fun `insertGenres should call dao insert with correct arguments`() = runTest {
        // arrange
        val genres = listOf(GenreDataModel(id = 1L, name = "Action"))

        // act
        sut.insertGenres(genres)

        // assert
        coVerify { dao.insert(genres.first()) }
    }

    @Test
    fun `insertMediaTypeGenres should call dao insert with correct arguments`() = runTest {
        // arrange
        val mtg = listOf(MediaTypeGenreDataModel(type = MediaDataType.MOVIE, genreId = 1L))

        // act
        sut.insertMediaTypeGenres(mtg)

        // assert
        coVerify { dao.insert(mtg.first()) }
    }

    @Test
    fun `getByMediaType should return list from dao`() = runTest {
        // arrange
        val expected = listOf(GenreDataModel(id = 1L, name = "Action"))
        coEvery { dao.getByMediaType(MediaDataType.MOVIE) } returns expected

        // act
        val result = sut.getByMediaType(MediaDataType.MOVIE)

        // assert
        assertEquals(expected, result)
        coVerify { dao.getByMediaType(MediaDataType.MOVIE) }
    }
}
