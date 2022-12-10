package br.com.deepbyte.overview.data.source.media

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.response.MediaDetailResponse
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.util.mock.ERROR_MSG
import br.com.deepbyte.overview.util.mock.ReturnType
import com.haroldadmin.cnradapter.NetworkResponse
import br.com.deepbyte.overview.util.mock.ReturnType.*
import br.com.deepbyte.overview.util.mock.mockResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

typealias MediaDetailsSuccess = NetworkResponse.Success<MediaDetailResponse>

// https://www.macoratti.net/20/12/net_unitconv1.htm
class MediaRemoteDataSourceTest {

    @MockK(relaxed = true)
    private lateinit var _api: ApiService
    private lateinit var _dataSource: IMediaRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        _dataSource = MediaRemoteDataSource(_api, _locale = mockk(relaxed = true))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_successResponse_dataResponseIsSameAsApi() = runTest {
        // Arrange
        val response = createMediaDetailsSuccess()
        coEveryMediaDetailResponse(requestType = SUCCESS, response)
        // Act
        val result = _dataSource.getItem(id = ID, type = TYPE)
        // Assert
        assertEquals(response.body, result.data)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_successResponse_resultIsSuccess() = runTest {
        // Arrange
        coEveryMediaDetailResponse(requestType = SUCCESS, createMediaDetailsSuccess())
        // Act
        val result = _dataSource.getItem(id = ID, type = TYPE)
        // Assert
        assertTrue(result is DataResult.Success)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_serverErrorResponse_messageResponseIsSameAsApi() = runTest {
        // Arrange
        coEveryMediaDetailResponse(requestType = SERVER_ERROR)
        // Act
        val result = _dataSource.getItem(id = ID, type = TYPE)
        // Assert
        assertEquals(ERROR_MSG, result.message)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_serverErrorResponse_resultIsServerError() = runTest {
        // Arrange
        coEveryMediaDetailResponse(requestType = SERVER_ERROR)
        // Act
        val result = _dataSource.getItem(id = ID, type = TYPE)
        // Assert
        assertTrue(result is DataResult.ServerError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_networkErrorResponse_resultIsNetworkError() = runTest {
        // Arrange
        coEveryMediaDetailResponse(requestType = NETWORK_ERROR)
        // Act
        val result = _dataSource.getItem(id = ID, type = TYPE)
        // Assert
        assertTrue(result is DataResult.NetworkError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_unknownErrorResponse_resultIsUnknownError() = runTest {
        // Arrange
        coEveryMediaDetailResponse(requestType = UNKNOWN_ERROR)
        // Act
        val result = _dataSource.getItem(id = ID, type = TYPE)
        // Assert
        assertTrue(result is DataResult.UnknownError)
    }

    private fun coEveryMediaDetailResponse(
        requestType: ReturnType,
        successResponse: MediaDetailsSuccess = createMediaDetailsSuccess()
    ) = coEvery {
        _api.getMediaItem(id = any(), type = any())
    } returns mockResponse(requestType, successResponse)

    private fun createMediaDetailsSuccess() =
        NetworkResponse.Success(body = MediaDetailResponse(), code = 200)

    private companion object {
        const val ID = 1L
        const val TYPE = "movie"
    }
}
