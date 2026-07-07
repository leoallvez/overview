package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.VideoDataModel
import br.dev.singular.overview.data.network.ApiService
import br.dev.singular.overview.data.network.response.ErrorResponse
import br.dev.singular.overview.data.network.response.ListResponse
import br.dev.singular.overview.data.util.createFakeVideoDataModelList
import com.haroldadmin.cnradapter.NetworkResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class VideoRemoteDataSourceTest {

    @MockK
    private lateinit var api: ApiService

    private lateinit var sut: IVideoRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = VideoRemoteDataSource(api)
    }

    @Test
    fun `getVideos should return DataResult Success when api returns success`() = runTest {
        // arrange
        val id = 1L
        val type = MediaDataType.MOVIE
        val videos = createFakeVideoDataModelList(count = 2)
        val response = ListResponse(results = videos)
        val successResponse = mockk<NetworkResponse.Success<ListResponse<VideoDataModel>>>()
        every { successResponse.body } returns response
        
        coEvery { api.getVideos(any(), any()) } returns successResponse

        // act
        val result = sut.getVideos(id, type)

        // assert
        assertTrue(result is DataResult.Success)
        assertEquals(videos, (result as DataResult.Success).data.results)
        coVerify(exactly = 1) { api.getVideos(mediaType = type.key, id = id) }
    }

    @Test
    fun `getVideos should return DataResult Error when api returns server error`() = runTest {
        // arrange
        val id = 1L
        val type = MediaDataType.TV
        coEvery { api.getVideos(any(), any()) } returns mockk<NetworkResponse.ServerError<ErrorResponse>>()

        // act
        val result = sut.getVideos(id, type)

        // assert
        assertTrue(result is DataResult.Error)
    }

    @Test
    fun `getVideos should return DataResult Error when api returns network error`() = runTest {
        // arrange
        val id = 1L
        val type = MediaDataType.MOVIE
        coEvery { api.getVideos(any(), any()) } returns mockk<NetworkResponse.NetworkError>()

        // act
        val result = sut.getVideos(id, type)

        // assert
        assertTrue(result is DataResult.Error)
    }

    @Test
    fun `getVideos should return DataResult Error when api returns unknown error`() = runTest {
        // arrange
        val id = 1L
        val type = MediaDataType.TV
        coEvery { api.getVideos(any(), any()) } returns mockk<NetworkResponse.UnknownError>()

        // act
        val result = sut.getVideos(id, type)

        // assert
        assertTrue(result is DataResult.Error)
    }
}
