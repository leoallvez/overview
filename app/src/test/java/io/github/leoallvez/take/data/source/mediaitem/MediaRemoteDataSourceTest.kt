package io.github.leoallvez.take.data.source.mediaitem

import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.source.ApiResult.*
import io.github.leoallvez.take.data.source.mock.*
import io.github.leoallvez.take.data.source.mock.MockResponseFactory.Companion.ERROR_MSG
import io.github.leoallvez.take.data.source.mock.MockResponseFactory.Companion.getDataResponse
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
    private lateinit var api: ApiService

    private lateinit var dataSource: IMediaRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        dataSource = MediaRemoteDataSource(api)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_successResponse_dataResponseIsSameAsApi() = runTest {
        //Arrange
        mockResponse<MediaDetailsSuccessResponse>()
        //Act
        val result = dataSource.getMediaDetailsResult(id = ID, type = TYPE)
        //Assert
        assertEquals(getDataResponse(), result.data)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_successResponse_resultIsSuccess() = runTest {
        //Arrange
        mockResponse<MediaDetailsSuccessResponse>()
        //Act
        val result = dataSource.getMediaDetailsResult(id = ID, type = TYPE)
        //Assert
        assertTrue(result is Success)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_serverErrorResponse_messageResponseIsSameAsApi() = runTest {
        //Arrange
        mockResponse<ServerErrorResponse>()
        //Act
        val result = dataSource.getMediaDetailsResult(id = ID, type = TYPE)
        //Assert
        assertEquals(ERROR_MSG, result.message)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_serverErrorResponse_resultIsServerError() = runTest {
        //Arrange
        mockResponse<ServerErrorResponse>()
        //Act
        val result = dataSource.getMediaDetailsResult(id = ID, type = TYPE)
        //Assert
        assertTrue(result is ServerError)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_networkErrorResponse_resultIsNetworkError() = runTest {
        //Arrange
        mockResponse<NetworkErrorResponse>()
        //Act
        val result = dataSource.getMediaDetailsResult(id = ID, type = TYPE)
        //Assert
        assertTrue(result is NetworkError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_unknownErrorResponse_resultIsUnknownError() = runTest {
        //Arrange
        mockResponse<UnknownErrorResponse>()
        //Act
        val result = dataSource.getMediaDetailsResult(id = ID, type = TYPE)
        //Assert
        assertTrue(result is UnknownError)
    }

    private inline fun <reified T : Response> mockResponse() {
        val factory = MockResponseFactory.createFactory<T>()
        coEvery { api.requestMediaDetail(any(), any()) } returns factory.makeResponse()
    }

    private companion object {
        const val ID = 1L
        const val TYPE = "movie"
    }
}
