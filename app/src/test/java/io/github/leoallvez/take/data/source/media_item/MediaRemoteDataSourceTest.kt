package io.github.leoallvez.take.data.source.media_item

import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.source.DataResult.*
import io.github.leoallvez.take.util.mock.*
import io.github.leoallvez.take.util.mock.MediaDetailsSuccessResponse
import io.github.leoallvez.take.util.mock.MockResponseFactory.Companion.ERROR_MSG
import io.github.leoallvez.take.util.mock.MockResponseFactory.Companion.getDataResponse
import io.github.leoallvez.take.util.mock.NetworkErrorResponse
import io.github.leoallvez.take.util.mock.ServerErrorResponse
import io.github.leoallvez.take.util.mock.UnknownErrorResponse
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

    private lateinit var _dataSource: IMediaRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        _dataSource = MediaRemoteDataSource(_api)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_successResponse_dataResponseIsSameAsApi() = runTest {
        //Arrange
        mockResponse<MediaDetailsSuccessResponse>()
        //Act
        val result = _dataSource.getMediaDetailsResult(apiId = ID, mediaType = TYPE)
        //Assert
        assertEquals(getDataResponse(), result.data)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_successResponse_resultIsSuccess() = runTest {
        //Arrange
        mockResponse<MediaDetailsSuccessResponse>()
        //Act
        val result = _dataSource.getMediaDetailsResult(apiId = ID, mediaType = TYPE)
        //Assert
        assertTrue(result is Success)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_serverErrorResponse_messageResponseIsSameAsApi() = runTest {
        //Arrange
        mockResponse<ServerErrorResponse>()
        //Act
        val result = _dataSource.getMediaDetailsResult(apiId = ID, mediaType = TYPE)
        //Assert
        assertEquals(ERROR_MSG, result.message)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_serverErrorResponse_resultIsServerError() = runTest {
        //Arrange
        mockResponse<ServerErrorResponse>()
        //Act
        val result = _dataSource.getMediaDetailsResult(apiId = ID, mediaType = TYPE)
        //Assert
        assertTrue(result is ServerError)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_networkErrorResponse_resultIsNetworkError() = runTest {
        //Arrange
        mockResponse<NetworkErrorResponse>()
        //Act
        val result = _dataSource.getMediaDetailsResult(apiId = ID, mediaType = TYPE)
        //Assert
        assertTrue(result is NetworkError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_unknownErrorResponse_resultIsUnknownError() = runTest {
        //Arrange
        mockResponse<UnknownErrorResponse>()
        //Act
        val result = _dataSource.getMediaDetailsResult(apiId = ID, mediaType = TYPE)
        //Assert
        assertTrue(result is UnknownError)
    }

    private inline fun <reified T : Response> mockResponse() {
        val factory = MockResponseFactory.createFactory<T>()
        coEvery { _api.getMediaDetail(any(), any()) } returns factory.makeResponse()
    }

    private companion object {
        const val ID = 1L
        const val TYPE = "movie"
    }

}
