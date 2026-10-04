package br.dev.singular.overview.data.repository.media

import br.dev.singular.overview.data.local.source.IMediaLocalDataSource
import br.dev.singular.overview.data.network.source.IAuthRemoteDataSource
import br.dev.singular.overview.data.network.source.IFavoriteRemoteDataSource
import br.dev.singular.overview.data.util.fakeMediaDataModel
import br.dev.singular.overview.data.util.fakeUserDataModel
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.data.util.mappers.domainToData.toData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class MediaFavoriteRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var authDataSource: IAuthRemoteDataSource

    @MockK(relaxed = true)
    private lateinit var remoteDataSource: IFavoriteRemoteDataSource

    @MockK(relaxed = true)
    private lateinit var localDataSource: IMediaLocalDataSource

    private lateinit var sut: MediaFavoriteRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { authDataSource.currentUser() } returns fakeUserDataModel
        sut = MediaFavoriteRepository(authDataSource, remoteDataSource, localDataSource)
    }

    @Test
    fun `update should save the favorite remotely and locally when media is liked`() = runTest {
        // Arrange
        val media = fakeMediaDataModel.toDomain().copy(isLiked = true)

        // Act
        sut.update(media)

        // Assert
        verify(exactly = 1) { remoteDataSource.save("uid", listOf(media.toData())) }
        verify(exactly = 0) { remoteDataSource.delete(any(), any()) }
        coVerify(exactly = 1) { localDataSource.update(media.toData()) }
    }

    @Test
    fun `update should delete the remote favorite when media is not liked`() = runTest {
        // Arrange
        val media = fakeMediaDataModel.toDomain().copy(isLiked = false)

        // Act
        sut.update(media)

        // Assert
        verify(exactly = 1) { remoteDataSource.delete("uid", media.toData()) }
        verify(exactly = 0) { remoteDataSource.save(any(), any()) }
        coVerify(exactly = 1) { localDataSource.update(media.toData()) }
    }

    @Test
    fun `update should throw and persist nothing when there is no session`() {
        // Arrange
        every { authDataSource.currentUser() } returns null

        // Act & Assert
        assertThrows(IllegalStateException::class.java) {
            runTest { sut.update(fakeMediaDataModel.toDomain()) }
        }
        verify(exactly = 0) { remoteDataSource.save(any(), any()) }
        coVerify(exactly = 0) { localDataSource.update(any()) }
    }

    @Test
    fun `getAll should return the remote favorites of the current user`() = runTest {
        // Arrange
        val favorites = listOf(fakeMediaDataModel.copy(isLiked = true))
        coEvery { remoteDataSource.getAll("uid") } returns favorites

        // Act
        val result = sut.getAll()

        // Assert
        assertEquals(favorites.map { it.toDomain() }, result)
    }

    @Test
    fun `getAll should throw when there is no session`() {
        // Arrange
        every { authDataSource.currentUser() } returns null

        // Act & Assert
        assertThrows(IllegalStateException::class.java) {
            runTest { sut.getAll() }
        }
        coVerify(exactly = 0) { remoteDataSource.getAll(any()) }
    }
}
