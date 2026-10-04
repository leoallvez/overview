package br.dev.singular.overview.data.repository.user

import androidx.datastore.preferences.core.Preferences
import br.dev.singular.overview.data.local.source.DataStoreDataSource
import br.dev.singular.overview.data.local.source.IMediaLocalDataSource
import br.dev.singular.overview.data.network.source.IAuthRemoteDataSource
import br.dev.singular.overview.data.network.source.IFavoriteRemoteDataSource
import br.dev.singular.overview.data.util.fakeMediaDataModel
import br.dev.singular.overview.data.util.fakeUserDataModel
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var dataSource: IAuthRemoteDataSource

    @MockK(relaxed = true)
    private lateinit var favoriteDataSource: IFavoriteRemoteDataSource

    @MockK(relaxed = true)
    private lateinit var mediaDataSource: IMediaLocalDataSource

    @MockK(relaxed = true)
    private lateinit var dataStoreDataSource: DataStoreDataSource

    private lateinit var sut: UserRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockFavoritesOwner(id = null)
        sut = UserRepository(dataSource, favoriteDataSource, mediaDataSource, dataStoreDataSource)
    }

    private fun mockFavoritesOwner(id: String?) {
        every { dataStoreDataSource.getValue(any<Preferences.Key<String>>()) } returns flowOf(id)
    }

    @Test
    fun `observe should map the session emitted by data source to domain`() = runTest {
        // Arrange
        every { dataSource.observe() } returns flowOf(null, fakeUserDataModel)

        // Act
        val result = sut.observe().toList()

        // Assert
        assertEquals(listOf(null, fakeUserDataModel.toDomain()), result)
    }

    @Test
    fun `getByParam should return User domain object when data source signs in`() = runTest {
        // Arrange
        coEvery { dataSource.signIn("token") } returns fakeUserDataModel

        // Act
        val result = sut.getByParam("token")

        // Assert
        assertEquals(fakeUserDataModel.toDomain(), result)
        coVerify(exactly = 1) { dataSource.signIn("token") }
    }

    @Test
    fun `getByParam should propagate exception when data source fails`() {
        // Arrange
        coEvery { dataSource.signIn(any()) } throws IllegalStateException("auth error")

        // Act & Assert
        assertThrows(IllegalStateException::class.java) {
            runTest { sut.getByParam("token") }
        }
    }

    @Test
    fun `get should return the current user mapped to domain`() = runTest {
        // Arrange
        every { dataSource.currentUser() } returns fakeUserDataModel

        // Act
        val result = sut.get()

        // Assert
        assertEquals(fakeUserDataModel.toDomain(), result)
    }

    @Test
    fun `get should return null when there is no session`() = runTest {
        // Arrange
        every { dataSource.currentUser() } returns null

        // Act & Assert
        assertNull(sut.get())
    }

    @Test
    fun `getByParam should upload the local favorites when they have no owner`() = runTest {
        // Arrange
        val liked = fakeMediaDataModel.copy(id = 1, isLiked = true)
        val notLiked = fakeMediaDataModel.copy(id = 2, isLiked = false)
        coEvery { dataSource.signIn("token") } returns fakeUserDataModel
        coEvery { mediaDataSource.getAll() } returns listOf(liked, notLiked)

        // Act
        sut.getByParam("token")

        // Assert
        verify(exactly = 1) { favoriteDataSource.save("uid", listOf(liked)) }
        coVerify(exactly = 0) { mediaDataSource.clearLiked() }
        coVerify(exactly = 1) { dataStoreDataSource.setValue(any<Preferences.Key<String>>(), "uid") }
    }

    @Test
    fun `getByParam should discard the local favorites left by another account`() = runTest {
        // Arrange
        mockFavoritesOwner(id = "other")
        coEvery { dataSource.signIn("token") } returns fakeUserDataModel
        coEvery { mediaDataSource.getAll() } returns listOf(fakeMediaDataModel.copy(isLiked = true))

        // Act
        sut.getByParam("token")

        // Assert
        coVerify(exactly = 1) { mediaDataSource.clearLiked() }
        verify(exactly = 0) { favoriteDataSource.save(any(), any()) }
        coVerify(exactly = 1) { dataStoreDataSource.setValue(any<Preferences.Key<String>>(), "uid") }
    }

    @Test
    fun `getByParam should keep the local favorites when the same account signs in again`() = runTest {
        // Arrange
        mockFavoritesOwner(id = "uid")
        coEvery { dataSource.signIn("token") } returns fakeUserDataModel

        // Act
        sut.getByParam("token")

        // Assert
        coVerify(exactly = 0) { mediaDataSource.clearLiked() }
        verify(exactly = 0) { favoriteDataSource.save(any(), any()) }
    }

    @Test
    fun `getByParam should return the user when the favorites upload fails`() = runTest {
        // Arrange
        coEvery { dataSource.signIn("token") } returns fakeUserDataModel
        coEvery { mediaDataSource.getAll() } throws IllegalStateException("upload error")

        // Act
        val result = sut.getByParam("token")

        // Assert
        assertEquals(fakeUserDataModel.toDomain(), result)
        verify(exactly = 0) { favoriteDataSource.save(any(), any()) }
    }

    @Test
    fun `clear should sign out and then clear the local favorites`() = runTest {
        // Act
        sut.clear()

        // Assert
        coVerifyOrder {
            dataSource.signOut()
            mediaDataSource.clearLiked()
        }
    }
}
