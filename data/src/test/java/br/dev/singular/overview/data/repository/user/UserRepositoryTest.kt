package br.dev.singular.overview.data.repository.user

import br.dev.singular.overview.data.local.source.IPredictionLocalDataSource
import br.dev.singular.overview.data.network.source.IAuthRemoteDataSource
import br.dev.singular.overview.data.util.fakeUserDataModel
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.impl.annotations.MockK
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
    private lateinit var predictionDataSource: IPredictionLocalDataSource

    private lateinit var sut: UserRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = UserRepository(dataSource, predictionDataSource)
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
    fun `execute should sign out and clear the cached predictions`() = runTest {
        // Act
        sut.execute()

        // Assert
        coVerifyOrder {
            dataSource.signOut()
            predictionDataSource.clear()
        }
    }
}
