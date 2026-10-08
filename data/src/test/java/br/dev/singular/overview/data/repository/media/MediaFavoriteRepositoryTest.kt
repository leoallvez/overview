package br.dev.singular.overview.data.repository.media

import br.dev.singular.overview.data.network.source.IAuthRemoteDataSource
import br.dev.singular.overview.data.network.source.IFavoriteRemoteDataSource
import br.dev.singular.overview.data.util.fakeMediaDataModel
import br.dev.singular.overview.data.util.fakeUserDataModel
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.data.util.mappers.domainToData.toData
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.util.Date

class MediaFavoriteRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var authDataSource: IAuthRemoteDataSource

    @MockK(relaxed = true)
    private lateinit var remoteDataSource: IFavoriteRemoteDataSource

    private lateinit var sut: MediaFavoriteRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { authDataSource.currentUser() } returns fakeUserDataModel
        sut = MediaFavoriteRepository(authDataSource, remoteDataSource)
    }

    @Test
    fun `update should save the favorite with the current date when media is liked`() = runTest {
        // Arrange
        val media = fakeMediaDataModel.toDomain().copy(isLiked = true, lastUpdate = Date(0))

        // Act
        sut.update(media)

        // Assert
        verify(exactly = 1) {
            remoteDataSource.save(
                "uid",
                match { models ->
                    val model = models.single()
                    model == media.toData().copy(lastUpdate = model.lastUpdate) &&
                            model.lastUpdate > Date(0)
                }
            )
        }
        verify(exactly = 0) { remoteDataSource.delete(any(), any()) }
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
        verify(exactly = 0) { remoteDataSource.delete(any(), any()) }
    }

    @Test
    fun `observe should emit the remote favorites of the signed in user`() = runTest {
        // Arrange
        val favorites = listOf(fakeMediaDataModel.copy(isLiked = true))
        every { authDataSource.observe() } returns flowOf(fakeUserDataModel)
        every { remoteDataSource.observe("uid") } returns flowOf(emptyList(), favorites)

        // Act
        val result = sut.observe().toList()

        // Assert
        assertEquals(listOf(emptyList(), favorites.map { it.toDomain() }), result)
    }

    @Test
    fun `observe should emit an empty list when there is no session`() = runTest {
        // Arrange
        every { authDataSource.observe() } returns flowOf(null)

        // Act
        val result = sut.observe().toList()

        // Assert
        assertEquals(listOf(emptyList<Any>()), result)
        verify(exactly = 0) { remoteDataSource.observe(any()) }
    }

    @Test
    fun `observe should follow the session changes`() = runTest {
        // Arrange
        val favorites = listOf(fakeMediaDataModel.copy(isLiked = true))
        every { authDataSource.observe() } returns flowOf(fakeUserDataModel, null)
        every { remoteDataSource.observe("uid") } returns flowOf(favorites)

        // Act
        val result = sut.observe().toList()

        // Assert
        assertEquals(emptyList<Any>(), result.last())
    }

    @Test
    fun `observe should not listen again when the same user is emitted twice`() = runTest {
        // Arrange
        every { authDataSource.observe() } returns flowOf(fakeUserDataModel, fakeUserDataModel)
        every { remoteDataSource.observe("uid") } returns flowOf(emptyList())

        // Act
        sut.observe().toList()

        // Assert
        verify(exactly = 1) { remoteDataSource.observe("uid") }
    }

    @Test
    fun `observe should emit an empty list when the remote listener fails`() = runTest {
        // Arrange
        every { authDataSource.observe() } returns flowOf(fakeUserDataModel)
        every { remoteDataSource.observe("uid") } returns flow {
            throw IllegalStateException("permission denied")
        }

        // Act
        val result = sut.observe().toList()

        // Assert
        assertEquals(listOf(emptyList<Any>()), result)
    }
}
