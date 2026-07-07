package br.dev.singular.overview.data.repository.media

import br.dev.singular.overview.data.local.source.IMediaLocalDataSource
import br.dev.singular.overview.data.model.MediaDataPage
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.util.fakeMediaDataModel
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.QueryState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MediaLocalRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var dataSource: IMediaLocalDataSource

    private lateinit var sut: MediaLocalRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = MediaLocalRepository(dataSource)
    }

    @Test
    fun `getAll should return list of media from data source`() = runTest {
        // Arrange
        coEvery { dataSource.getAll() } returns listOf(fakeMediaDataModel)

        // Act
        val result = sut.getAll()

        // Assert
        assertEquals(1, result.size)
        coVerify { dataSource.getAll() }
    }

    @Test
    fun `getPage should return media page from data source`() = runTest {
        // Arrange
        val queryState = QueryState(page = 1, isLiked = true, type = MediaType.MOVIE)
        coEvery { dataSource.getPage(any(), any(), any()) } returns MediaDataPage(
            items = listOf(
                fakeMediaDataModel
            )
        )

        // Act
        val result = sut.getPage(queryState)

        // Assert
        assertEquals(1, result.items.size)
        coVerify { dataSource.getPage(1, true, MediaDataType.MOVIE) }
    }

    @Test
    fun `delete should call data source delete`() = runTest {
        // Arrange
        val media = fakeMediaDataModel.toDomain()

        // Act
        sut.delete(media)

        // Assert
        coVerify { dataSource.delete(any()) }
    }

    @Test
    fun `getById should return media from data source`() = runTest {
        // Arrange
        coEvery { dataSource.getById(any()) } returns fakeMediaDataModel

        // Act
        val result = sut.getById(1)

        // Assert
        coVerify(exactly = 1) { dataSource.getById(1L) }
        assertEquals(fakeMediaDataModel.toDomain(), result)
    }

    @Test
    fun `update should call data source update`() = runTest {
        // Arrange
        val media = fakeMediaDataModel.toDomain()

        // Act
        sut.update(media)

        // Assert
        coVerify { dataSource.update(any()) }
    }
}
