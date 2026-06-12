package br.dev.singular.overview.data.repository

import br.dev.singular.overview.data.local.source.IGenreLocalDataSource
import br.dev.singular.overview.data.model.GenreDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.domain.model.Genre
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.MediaTypeGenres
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GenreLocalRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var dataSource: IGenreLocalDataSource

    private lateinit var sut: GenreLocalRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = GenreLocalRepository(dataSource)
    }

    @Test
    fun `getByParam should return list of genres from data source`() = runTest {
        // Arrange
        val mediaType = MediaType.MOVIE
        val genreDataModels = listOf(GenreDataModel(id = 1, name = "Action"))
        coEvery { dataSource.getByMediaType(MediaDataType.MOVIE) } returns genreDataModels

        // Act
        val result = sut.getByParam(mediaType)

        // Assert
        assertEquals(1, result.size)
        assertEquals("Action", result.first().name)
        coVerify { dataSource.getByMediaType(MediaDataType.MOVIE) }
    }

    @Test
    fun `update should insert genres and media type genres`() = runTest {
        // Arrange
        val genres = listOf(Genre(id = 1, name = "Action"))
        val mediaTypeGenres = MediaTypeGenres(type = MediaType.MOVIE, genres = genres)

        // Act
        sut.update(mediaTypeGenres)

        // Assert
        coVerify {
            dataSource.insertGenres(any())
            dataSource.insertMediaTypeGenres(any())
        }
    }
}
