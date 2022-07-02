package io.github.leoallvez.take.data.source.mediaitem

import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.source.mock.MockResponseFactory
import io.github.leoallvez.take.data.source.mock.MockResponseFactory.Companion.createResponse
import io.github.leoallvez.take.data.source.mock.SuccessResponse
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

    private lateinit var dataSource: IMediaRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        dataSource = MediaRemoteDataSource(_api)
    }

    @Test
    fun lab() = runTest {

        val mockFactory = MockResponseFactory.createFactory<SuccessResponse>()
        coEvery {
            _api.getMediaDetail(id = any(), type = any())
        } returns mockFactory.makeResponse()

        val response = dataSource.getMediaDetails(id = 100, type = "movie")

        assertEquals(createResponse(), response.data)
    }

}

