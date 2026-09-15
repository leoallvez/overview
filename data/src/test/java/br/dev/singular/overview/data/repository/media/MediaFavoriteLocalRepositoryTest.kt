package br.dev.singular.overview.data.repository.media

import br.dev.singular.overview.data.local.source.IMediaLocalDataSource
import br.dev.singular.overview.data.util.fakeMediaDataModel
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.data.util.mappers.domainToData.toData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MediaFavoriteLocalRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var dataSource: IMediaLocalDataSource

    private lateinit var sut: MediaFavoriteLocalRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = MediaFavoriteLocalRepository(dataSource)
    }

    @Test
    fun `getAll should return only the liked medias mapped to domain`() = runTest {
        // Arrange
        val liked = fakeMediaDataModel.copy(id = 1, isLiked = true)
        val notLiked = fakeMediaDataModel.copy(id = 2, isLiked = false)
        coEvery { dataSource.getAll() } returns listOf(liked, notLiked)

        // Act
        val result = sut.getAll()

        // Assert
        assertEquals(listOf(liked.toDomain()), result)
    }

    @Test
    fun `update should insert the medias keeping their last update`() = runTest {
        // Arrange
        val medias = listOf(
            fakeMediaDataModel.copy(id = 1, isLiked = true).toDomain(),
            fakeMediaDataModel.copy(id = 2, isLiked = false).toDomain()
        )

        // Act
        sut.update(medias)

        // Assert
        coVerify(exactly = 1) { dataSource.insert(medias.map { it.toData() }) }
        coVerify(exactly = 0) { dataSource.update(any()) }
    }
}
