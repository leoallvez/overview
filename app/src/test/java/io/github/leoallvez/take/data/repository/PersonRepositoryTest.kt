package io.github.leoallvez.take.data.repository

import io.github.leoallvez.take.data.api.response.PersonResponse
import io.github.leoallvez.take.data.source.DataResult
import io.github.leoallvez.take.data.source.person.IPersonRemoteDataSource
import io.github.leoallvez.take.util.mock.ReturnType
import io.github.leoallvez.take.util.mock.ReturnType.*
import io.github.leoallvez.take.util.mock.mockResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getPersonDetails_success() = runTest {
        // Arrange
        coEveryPersonResponse(requestType = SUCCESS)
        // Act
        val result = _repository.getPersonDetails(apiId = 1).first()
        // Assert
        Assert.assertTrue(result is DataResult.Success)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getPersonDetails_serverError() = runTest {
        // Arrange
        coEveryPersonResponse(requestType = SERVER_ERROR)
        // Act
        val result = _repository.getPersonDetails(apiId = 1).first()
        // Assert
        Assert.assertTrue(result is DataResult.ServerError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getPersonDetails_networkError() = runTest {
        // Arrange
        coEveryPersonResponse(requestType = NETWORK_ERROR)
        // Act
        val result = _repository.getPersonDetails(apiId = 1).first()
        // Assert
        Assert.assertTrue(result is DataResult.NetworkError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getPersonDetails_unknownError() = runTest {
        // Arrange
        coEveryPersonResponse(requestType = UNKNOWN_ERROR)
        // Act
        val result = _repository.getPersonDetails(apiId = 1).first()
        // Assert
        Assert.assertTrue(result is DataResult.UnknownError)
    }
    private fun coEveryPersonResponse(requestType: ReturnType) = coEvery {
        _source.getPersonDetails(any())
    } returns mockResult(requestType, DataResult.Success(data = PersonResponse()))
}