package br.com.deepbyte.overview.data.source.person

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.model.person.Person
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.util.mock.ERROR_MSG
import br.com.deepbyte.overview.util.mock.ReturnType
import br.com.deepbyte.overview.util.mock.ReturnType.NETWORK_ERROR
import br.com.deepbyte.overview.util.mock.ReturnType.SERVER_ERROR
import br.com.deepbyte.overview.util.mock.ReturnType.SUCCESS
import br.com.deepbyte.overview.util.mock.ReturnType.UNKNOWN_ERROR
import br.com.deepbyte.overview.util.mock.mockResponse
import com.haroldadmin.cnradapter.NetworkResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test

private typealias PersonSuccess = NetworkResponse.Success<Person>

class PersonRemoteDataSourceTest {

    @MockK(relaxed = true)
    private lateinit var _api: ApiService
    private lateinit var _dataSource: IPersonRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        _dataSource = PersonRemoteDataSource(_api, _locale = mockk(relaxed = true))
    }

    @Test
    fun `should be equals response and result when call get item`() = runTest {
        // Arrange
        val response = createPersonResponseSuccess()
        coEveryPersonResponse(requestType = SUCCESS, response)
        // Act
        val result = _dataSource.getItem(apiId = 1)
        // Assert
        response.body.shouldBeEqualTo(result.data)
    }

    @Test
    fun `should be instance of success when request type is success`() = runTest {
        // Arrange
        coEveryPersonResponse(requestType = SUCCESS)
        // Act
        val result = _dataSource.getItem(apiId = 1)
        // Assert
        result.shouldBeInstanceOf<DataResult.Success<Person>>()
    }

    @Test
    fun `should have a message error when result is serve error`() = runTest {
        // Arrange
        coEveryPersonResponse(requestType = SERVER_ERROR)
        // Act
        val result = _dataSource.getItem(apiId = 1)
        // Assert
        ERROR_MSG.shouldBeEqualTo(result.message)
    }

    @Test
    fun `should be instance of serve error when request type is serve error`() = runTest {
        // Arrange
        coEveryPersonResponse(requestType = SERVER_ERROR)
        // Act
        val result = _dataSource.getItem(apiId = 1)
        // Assert
        result.shouldBeInstanceOf<DataResult.ServerError<Person>>()
    }

    @Test
    fun `should be instance of network error when request type is network error`() = runTest {
        // Arrange
        coEveryPersonResponse(requestType = NETWORK_ERROR)
        // Act
        val result = _dataSource.getItem(apiId = 1)
        // Assert
        result.shouldBeInstanceOf<DataResult.NetworkError<Person>>()
    }

    @Test
    fun `should be instance of unknown error when request type is unknown error`() = runTest {
        // Arrange
        coEveryPersonResponse(requestType = UNKNOWN_ERROR)
        // Act
        val result = _dataSource.getItem(apiId = 1)
        // Assert
        result.shouldBeInstanceOf<DataResult.UnknownError<Person>>()
    }

    private fun coEveryPersonResponse(
        requestType: ReturnType,
        successResponse: PersonSuccess = createPersonResponseSuccess()
    ) = coEvery {
        _api.getPersonItem(id = any())
    } returns mockResponse(requestType, successResponse)

    private fun createPersonResponseSuccess() =
        NetworkResponse.Success(body = Person(), code = 200)
}
