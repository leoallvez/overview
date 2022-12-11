package br.com.deepbyte.overview.data.source.provider

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.response.ProviderResponse
import br.com.deepbyte.overview.data.source.DataResult
import com.haroldadmin.cnradapter.NetworkResponse
import br.com.deepbyte.overview.util.mock.ERROR_MSG
import br.com.deepbyte.overview.util.mock.ReturnType
import br.com.deepbyte.overview.util.mock.mockResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

typealias ProviderSuccess = NetworkResponse.Success<ProviderResponse>

class ProviderRemoteDataSourceTest {

    @MockK(relaxed = true)
    private lateinit var _api: ApiService
    private lateinit var _dataSource: IProviderRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        _dataSource = ProviderRemoteDataSource(_api, _locale = mockk(relaxed = true))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getProvidersResult_successResponse_dataResponseIsSameAsApi() = runTest {
        // Arrange
        val response = createProviderResponseSuccess()
        coEveryProviderResponse(requestType = ReturnType.SUCCESS, response)
        // Act
        val result = _dataSource.getItems(apiId = 1, type = "movie")
        // Assert
        Assert.assertEquals(response.body, result.data)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getProvidersResult_successResponse_resultIsSuccess() = runTest {
        // Arrange
        coEveryProviderResponse(requestType = ReturnType.SUCCESS)
        // Act
        val result = _dataSource.getItems(apiId = 1, type = "movie")
        // Assert
        Assert.assertTrue(result is DataResult.Success)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getProvidersResult_serverErrorResponse_messageResponseIsSameAsApi() = runTest {
        // Arrange
        coEveryProviderResponse(requestType = ReturnType.SERVER_ERROR)
        // Act
        val result = _dataSource.getItems(apiId = 1, type = "movie")
        // Assert
        Assert.assertEquals(ERROR_MSG, result.message)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getProvidersResult_serverErrorResponse_resultIsServerError() = runTest {
        // Arrange
        coEveryProviderResponse(requestType = ReturnType.SERVER_ERROR)
        // Act
        val result = _dataSource.getItems(apiId = 1, type = "movie")
        // Assert
        Assert.assertTrue(result is DataResult.ServerError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getProvidersResult_networkErrorResponse_resultIsNetworkError() = runTest {
        // Arrange
        coEveryProviderResponse(requestType = ReturnType.NETWORK_ERROR)
        // Act
        val result = _dataSource.getItems(apiId = 1, type = "movie")
        // Assert
        Assert.assertTrue(result is DataResult.NetworkError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getProvidersResult_unknownErrorResponse_resultIsUnknownError() = runTest {
        // Arrange
        coEveryProviderResponse(requestType = ReturnType.UNKNOWN_ERROR)
        // Act
        val result = _dataSource.getItems(apiId = 1, type = "movie")
        // Assert
        Assert.assertTrue(result is DataResult.UnknownError)
    }

    private fun coEveryProviderResponse(
        requestType: ReturnType,
        successResponse: ProviderSuccess = createProviderResponseSuccess()
    ) = coEvery {
        _api.getProviders(id = any(), type = any())
    } returns mockResponse(requestType, successResponse)

    private fun createProviderResponseSuccess() =
        NetworkResponse.Success(body = ProviderResponse(), code = 200)
}
