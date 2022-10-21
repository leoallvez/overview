package io.github.leoallvez.take.data.source.person

import com.haroldadmin.cnradapter.NetworkResponse
import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.api.response.PersonResponse
import io.github.leoallvez.take.data.source.DataResult
import io.github.leoallvez.take.util.mock.*
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

typealias PersonSuccess = NetworkResponse.Success<PersonResponse>

class PersonRemoteDataSourceTest {

    @MockK(relaxed = true)
    private lateinit var _api: ApiService
    private lateinit var _dataSource: IPersonRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        _dataSource = PersonRemoteDataSource(_api, _locale = mockk(relaxed = true))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getPersonDetails_successResponse_dataResponseIsSameAsApi() = runTest {
        //Arrange
        val response = createPersonResponseSuccess()
        coEveryPersonResponse(ReturnType.SUCCESS, response)
        //Act
        val result = _dataSource.getPersonDetails(apiId = 1)
        //Assert
        Assert.assertEquals(response.body, result.data)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getPersonDetails_successResponse_resultIsSuccess() = runTest {
        //Arrange
        coEveryPersonResponse(ReturnType.SUCCESS)
        //Act
        val result = _dataSource.getPersonDetails(apiId = 1)
        //Assert
        Assert.assertTrue(result is DataResult.Success)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getPersonDetails_serverErrorResponse_messageResponseIsSameAsApi() = runTest {
        //Arrange
        coEveryPersonResponse(ReturnType.SERVER_ERROR)
        //Act
        val result = _dataSource.getPersonDetails(apiId = 1)
        //Assert
        Assert.assertEquals(ERROR_MSG, result.message)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getPersonDetails_serverErrorResponse_resultIsServerError() = runTest {
        //Arrange
        coEveryPersonResponse(ReturnType.SERVER_ERROR)
        //Act
        val result = _dataSource.getPersonDetails(apiId = 1)
        //Assert
        Assert.assertTrue(result is DataResult.ServerError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getPersonDetails_networkErrorResponse_resultIsNetworkError() = runTest {
        //Arrange
        coEveryPersonResponse(ReturnType.NETWORK_ERROR)
        //Act
        val result = _dataSource.getPersonDetails(apiId = 1)
        //Assert
        Assert.assertTrue(result is DataResult.NetworkError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getPersonDetails_unknownErrorResponse_resultIsUnknownError() = runTest {
        //Arrange
        coEveryPersonResponse(ReturnType.UNKNOWN_ERROR)
        //Act
        val result = _dataSource.getPersonDetails(apiId = 1)
        //Assert
        Assert.assertTrue(result is DataResult.UnknownError)
    }

    private fun coEveryPersonResponse(
        requestType: ReturnType,
        successResponse: PersonSuccess = createPersonResponseSuccess()
    ) = coEvery {
        _api.getPersonDetails(id = any())
    } returns mockResponse(requestType, successResponse)

    private fun createPersonResponseSuccess() =
        NetworkResponse.Success(body = PersonResponse(), code = 200)
}
