package io.github.leoallvez.take.data.source.person

import com.haroldadmin.cnradapter.NetworkResponse
import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.api.IApiLocale
import io.github.leoallvez.take.data.api.response.ErrorResponse
import io.github.leoallvez.take.data.api.response.PersonResponse
import io.github.leoallvez.take.data.source.DataResult
import io.github.leoallvez.take.util.mock.*
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PersonRemoteDataSourceTest {

    @MockK(relaxed = true)
    private lateinit var _api: ApiService

    @MockK(relaxed = true)
    private lateinit var _locale: IApiLocale

    private lateinit var _dataSource: IPersonRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        _dataSource = PersonRemoteDataSource(_api, _locale)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getPersonDetails_successResponse_dataResponseIsSameAsApi() = runTest {
        //Arrange
        val response = NetworkResponse.Success(body = PersonResponse(), code = 200)
        coEveryApi(response)
        //Act
        val result = _dataSource.getPersonDetails(apiId = 1)
        //Assert
        Assert.assertEquals(response.body, result.data)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getPersonDetails_successResponse_resultIsSuccess() = runTest {
        //Arrange
        val response = NetworkResponse.Success(body = PersonResponse(), code = 200)
        coEveryApi(response)
        //Act
        val result = _dataSource.getPersonDetails(apiId = 1)
        //Assert
        Assert.assertTrue(result is DataResult.Success)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getPersonDetails_serverErrorResponse_messageResponseIsSameAsApi() = runTest {
        //Arrange
        val response = MockResponse.serverErrorResponse
        coEveryApi(response)
        //Act
        val result = _dataSource.getPersonDetails(apiId = 1)
        //Assert
        Assert.assertEquals(MockResponse.ERROR_MSG, result.message)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getPersonDetails_serverErrorResponse_resultIsServerError() = runTest {
        //Arrange
        val response = MockResponse.serverErrorResponse
        coEveryApi(response)
        //Act
        val result = _dataSource.getPersonDetails(apiId = 1)
        //Assert
        Assert.assertTrue(result is DataResult.ServerError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getPersonDetails_networkErrorResponse_resultIsNetworkError() = runTest {
        //Arrange
        val response = MockResponse.networkResponse
        coEveryApi(response)
        //Act
        val result = _dataSource.getPersonDetails(apiId = 1)
        //Assert
        Assert.assertTrue(result is DataResult.NetworkError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getPersonDetails_unknownErrorResponse_resultIsUnknownError() = runTest {
        //Arrange
        val response = MockResponse.unknownErrorResponse
        coEveryApi(response)
        //Act
        val result = _dataSource.getPersonDetails(apiId = 1)
        //Assert
        Assert.assertTrue(result is DataResult.UnknownError)
    }

    private fun coEveryApi(response: NetworkResponse<PersonResponse, ErrorResponse>) {
        coEvery { _api.getPersonDetails(id = any()) } returns response
    }
}
