package io.github.leoallvez.take.data.source.mediaitem

import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.source.mock.MockResponseFactory
import io.github.leoallvez.take.data.source.mock.MockResponseFactory.Companion.getDataResponse
import io.github.leoallvez.take.data.source.mock.Response
import io.github.leoallvez.take.data.source.mock.MediaDetailsSuccessResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MediaRemoteDataSourceTest {

    @MockK(relaxed = true)
    private lateinit var _api: ApiService

    private lateinit var _dataSource: IMediaRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        _dataSource = MediaRemoteDataSource(_api)
    }

    @Test
    fun lab() = runTest {

        mockResponse<MediaDetailsSuccessResponse>()

        val response = _dataSource.getMediaDetails(id = 1, type = "movie")

        assertEquals(getDataResponse(), response.data)
    }

    private inline fun <reified T : Response> mockResponse() {
        val factory = MockResponseFactory.createFactory<T>()
        coEvery { _api.requestMediaDetail(any(), any()) } returns factory.makeResponse()
    }
}
