package dev.com.singular.overview.data.repository.person

import dev.com.singular.overview.util.mock.ReturnType
import dev.com.singular.overview.util.mock.ReturnType.*
import dev.com.singular.overview.util.mock.mockResult
import dev.com.singular.overview.data.model.person.Person
import dev.com.singular.overview.data.source.DataResult
import dev.com.singular.overview.data.source.person.IPersonRemoteDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test

class PersonRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var _source: IPersonRemoteDataSource

    private lateinit var _repository: PersonRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockKAnnotations.init(this)
        _repository = PersonRepository(_source, UnconfinedTestDispatcher())
    }

    @Test
    fun `should be instance of success when request type is success`() = runTest {
        // Arrange
        coEveryPersonResponse(requestType = SUCCESS)
        // Act
        val result = _repository.getItem(apiId = 1).first()
        // Assert
        result.shouldBeInstanceOf<DataResult.Success<Person>>()
    }

    @Test
    fun `should be instance of serve error when request type is serve error`() = runTest {
        // Arrange
        coEveryPersonResponse(requestType = SERVER_ERROR)
        // Act
        val result = _repository.getItem(apiId = 1).first()
        // Assert
        result.shouldBeInstanceOf<DataResult.ServerError<Person>>()
    }

    @Test
    fun `should be instance of network error when request type is network error`() = runTest {
        // Arrange
        coEveryPersonResponse(requestType = NETWORK_ERROR)
        // Act
        val result = _repository.getItem(apiId = 1).first()
        // Assert
        result.shouldBeInstanceOf<DataResult.NetworkError<Person>>()
    }

    @Test
    fun `should be instance of unknown error when request type is unknown error`() = runTest {
        // Arrange
        coEveryPersonResponse(requestType = UNKNOWN_ERROR)
        // Act
        val result = _repository.getItem(apiId = 1).first()
        // Assert
        result.shouldBeInstanceOf<DataResult.UnknownError<Person>>()
    }

    private fun coEveryPersonResponse(
        requestType: ReturnType
    ) = coEvery {
        _source.getItem(any())
    } returns mockResult(requestType, DataResult.Success(data = Person()))
}
