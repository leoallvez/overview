package io.github.leoallvez.take.data.source.media_item

import com.haroldadmin.cnradapter.NetworkResponse
import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.api.IApiLocale
import io.github.leoallvez.take.data.api.response.ErrorResponse
import io.github.leoallvez.take.data.api.response.MediaDetailResponse
import io.github.leoallvez.take.data.source.DataResult.*
import io.github.leoallvez.take.util.mock.*
import io.github.leoallvez.take.util.mock.MockResponse.ERROR_MSG
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

//https://www.macoratti.net/20/12/net_unitconv1.htm
class MediaRemoteDataSourceTest {

    @MockK(relaxed = true)
    private lateinit var _api: ApiService

    @MockK(relaxed = true)
    private lateinit var _locale: IApiLocale

    private lateinit var _dataSource: IMediaRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        _dataSource = MediaRemoteDataSource(_api, _locale)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_successResponse_dataResponseIsSameAsApi() = runTest {
        //Arrange
        val response = NetworkResponse.Success(body = MediaDetailResponse(), code = 200)
        coEveryApi(response)
        //Act
        val result = _dataSource.getMediaDetailsResult(id = ID, type = TYPE)
        //Assert
        assertEquals(response.body, result.data)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_successResponse_resultIsSuccess() = runTest {
        //Arrange
        val response = NetworkResponse.Success(body = MediaDetailResponse(), code = 200)
        coEveryApi(response)
        //Act
        val result = _dataSource.getMediaDetailsResult(id = ID, type = TYPE)
        //Assert
        assertTrue(result is Success)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_serverErrorResponse_messageResponseIsSameAsApi() = runTest {
        //Arrange
        coEveryApi(response = MockResponse.serverErrorResponse)
        //Act
        val result = _dataSource.getMediaDetailsResult(id = ID, type = TYPE)
        //Assert
        assertEquals(ERROR_MSG, result.message)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_serverErrorResponse_resultIsServerError() = runTest {
        //Arrange
        coEveryApi(response = MockResponse.serverErrorResponse)
        //Act
        val result = _dataSource.getMediaDetailsResult(id = ID, type = TYPE)
        //Assert
        assertTrue(result is ServerError)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_networkErrorResponse_resultIsNetworkError() = runTest {
        //Arrange
        coEveryApi(response = MockResponse.networkResponse)
        //Act
        val result = _dataSource.getMediaDetailsResult(id = ID, type = TYPE)
        //Assert
        assertTrue(result is NetworkError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_unknownErrorResponse_resultIsUnknownError() = runTest {
        //Arrange
        coEveryApi(response = MockResponse.unknownErrorResponse)
        //Act
        val result = _dataSource.getMediaDetailsResult(id = ID, type = TYPE)
        //Assert
        assertTrue(result is UnknownError)
    }

    private fun coEveryApi(response: NetworkResponse<MediaDetailResponse, ErrorResponse>) {
        coEvery { _api.getMediaDetail(id = any(), type = any()) } returns response
    }

    private companion object {
        const val ID = 1L
        const val TYPE = "movie"
    }

}
