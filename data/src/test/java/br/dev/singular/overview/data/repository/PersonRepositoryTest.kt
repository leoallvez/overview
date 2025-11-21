package br.dev.singular.overview.data.repository

import br.dev.singular.overview.data.model.PersonDataModel
import br.dev.singular.overview.data.network.source.DataResult
import br.dev.singular.overview.data.network.source.IPersonRemoteDataSource
import br.dev.singular.overview.data.util.fakePersonDataModel
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PersonRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var remoteDataSource: IPersonRemoteDataSource

    private lateinit var sut: PersonRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = PersonRepository(remoteDataSource)
    }

    @Test
    fun `should return Person domain object when data source returns success`() = runTest {
        // Arrange
        val successResult = DataResult.Success(fakePersonDataModel)
        coEvery { remoteDataSource.getById(any()) } returns successResult

        // Act
        val result = sut.getById(0)

        // Assert
        assertEquals(fakePersonDataModel.toDomain(), result)
    }

    @Test
    fun `should return null when data source returns an error`() = runTest {
        // Arrange
        val errorResult = DataResult.Error<PersonDataModel>("Not Found")
        coEvery { remoteDataSource.getById(any()) } returns errorResult

        // Act
        val result = sut.getById(0)

        // Assert
        assertNull(result)
    }

    @Test
    fun `should return null when data source throws a general exception`() = runTest {
        val errorResult = DataResult.Error<PersonDataModel>("Network Failure")
        coEvery { remoteDataSource.getById(any()) } returns errorResult

        // Act
        val result = sut.getById(0)

        // Assert
        assertNull(result)
    }
}
