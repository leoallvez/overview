package io.github.leoallvez.take.data.source.mediaitem

import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.source.NetworkResult.*
import io.github.leoallvez.take.data.source.mock.MediaDetailsSuccessResponse
import io.github.leoallvez.take.data.source.mock.MockResponseFactory
import io.github.leoallvez.take.data.source.mock.MockResponseFactory.Companion.getDataResponse
import io.github.leoallvez.take.data.source.mock.Response
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
    @Test fun getMediaDetailsResult_dataResponseIsSameAsApi_successResponse() = runTest {
        //Arrange
        mockResponse<MediaDetailsSuccessResponse>()
        //Act
        val result = _dataSource.getMediaDetailsResult(id = 1, type = "movie")
        //Assert
        assertEquals(getDataResponse(), result.data)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_resultIsTypeSuccess_successResponse() = runTest {
        //Arrange
        mockResponse<MediaDetailsSuccessResponse>()
        //Act
        val result = _dataSource.getMediaDetailsResult(id = 1, type = "movie")
        //Assert
        assertTrue(result is Success)
    }

    private inline fun <reified T : Response> mockResponse() {
        val factory = MockResponseFactory.createFactory<T>()
        coEvery { _api.requestMediaDetail(any(), any()) } returns factory.makeResponse()
    }
}
