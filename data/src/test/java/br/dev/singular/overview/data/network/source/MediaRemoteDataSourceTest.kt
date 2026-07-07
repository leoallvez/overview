package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.MediaDataPage
import br.dev.singular.overview.data.model.MovieDetailsDataModel
import br.dev.singular.overview.data.model.QueryDataState
import br.dev.singular.overview.data.model.TvShowDetailsDataModel
import br.dev.singular.overview.data.network.ApiService
import br.dev.singular.overview.data.network.response.ErrorResponse
import br.dev.singular.overview.data.util.fakeMovieDetailsDataModel
import br.dev.singular.overview.data.util.fakeTvShowDetailsDataModel
import com.haroldadmin.cnradapter.NetworkResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MediaRemoteDataSourceTest {

    @MockK
    private lateinit var api: ApiService

    private lateinit var sut: IMediaRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = MediaRemoteDataSource(api)
    }

    @Test
    fun `getByQuery should return Success when API returns success`() = runTest {
        // arrange
        val queryState = QueryDataState(path = "movie/popular", page = 1)
        val page = MediaDataPage(page = 1, items = emptyList())
        val successResponse = mockk<NetworkResponse.Success<MediaDataPage>>()
        every { successResponse.body } returns page
        
        coEvery { 
            api.fetchMediaPage(any(), any(), any(), any()) 
        } returns successResponse

        // act
        val result = sut.getByQuery(queryState, emptyMap())

        // assert
        assertTrue(result is DataResult.Success)
        (result as DataResult.Success).data shouldBeEqualTo page
        coVerify(exactly = 1) { 
            api.fetchMediaPage(
                path = "movie/popular",
                page = 1,
                query = null,
                options = any()
            ) 
        }
    }

    @Test
    fun `getByQuery should return Error when API returns error`() = runTest {
        // arrange
        val queryState = QueryDataState(path = "movie/popular")
        coEvery { 
            api.fetchMediaPage(any(), any(), any(), any()) 
        } returns mockk<NetworkResponse.UnknownError>()

        // act
        val result = sut.getByQuery(queryState, emptyMap())

        // assert
        assertTrue(result is DataResult.Error)
    }

    @Test
    fun `getMovieById should return Success when API returns success`() = runTest {
        // arrange
        val id = 123L
        val successResponse = mockk<NetworkResponse.Success<MovieDetailsDataModel>>()
        every { successResponse.body } returns fakeMovieDetailsDataModel
        
        coEvery { api.getMovieDetailsById(id = id) } returns successResponse

        // act
        val result = sut.getMovieById(id)

        // assert
        assertTrue(result is DataResult.Success)
        (result as DataResult.Success).data shouldBeEqualTo fakeMovieDetailsDataModel
        coVerify(exactly = 1) { api.getMovieDetailsById(id = id) }
    }

    @Test
    fun `getMovieById should return Error when API returns error`() = runTest {
        // arrange
        val id = 123L
        coEvery { api.getMovieDetailsById(id = id) } returns mockk<NetworkResponse.ServerError<ErrorResponse>>()

        // act
        val result = sut.getMovieById(id)

        // assert
        assertTrue(result is DataResult.Error)
    }

    @Test
    fun `getTvShowById should return Success when API returns success`() = runTest {
        // arrange
        val id = 456L
        val successResponse = mockk<NetworkResponse.Success<TvShowDetailsDataModel>>()
        every { successResponse.body } returns fakeTvShowDetailsDataModel
        
        coEvery { api.getTvShowDetailsById(id = id) } returns successResponse

        // act
        val result = sut.getTvShowById(id)

        // assert
        assertTrue(result is DataResult.Success)
        (result as DataResult.Success).data shouldBeEqualTo fakeTvShowDetailsDataModel
        coVerify(exactly = 1) { api.getTvShowDetailsById(id = id) }
    }

    @Test
    fun `getTvShowById should return Error when API returns error`() = runTest {
        // arrange
        val id = 456L
        coEvery { api.getTvShowDetailsById(id = id) } returns mockk<NetworkResponse.NetworkError>()

        // act
        val result = sut.getTvShowById(id)

        // assert
        assertTrue(result is DataResult.Error)
    }
}
