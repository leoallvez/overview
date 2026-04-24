package br.dev.singular.overview.data.repository

import br.dev.singular.overview.data.model.GenreDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.network.source.IGenreRemoteDataSource
import br.dev.singular.overview.domain.model.MediaType
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GenreRemoteRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var dataSource: IGenreRemoteDataSource

    private lateinit var sut: GenreRemoteRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = GenreRemoteRepository(dataSource)
    }

    @Test
    fun `getByParam should return list of genres from remote data source`() = runTest {
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
}
