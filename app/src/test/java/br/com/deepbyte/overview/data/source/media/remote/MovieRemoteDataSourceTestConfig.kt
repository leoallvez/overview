package br.com.deepbyte.overview.data.source.media.remote

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.util.mock.ERROR_MSG
import br.com.deepbyte.overview.util.mock.ReturnType
import br.com.deepbyte.overview.util.mock.mockResponse
import com.haroldadmin.cnradapter.NetworkResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test

private typealias MovieSuccess = NetworkResponse.Success<Movie>

class MovieRemoteDataSourceTestConfig {

    @MockK(relaxed = true)
    private lateinit var _api: ApiService
    private lateinit var _dataSource: IMediaRemoteDataSource<Movie>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        _dataSource = MovieRemoteDataSource(_api, _locale = mockk(relaxed = true))
    }

    @Test
    fun `should be equals response and result when call get item`() = runTest {
        // Arrange
        val response = createMovieResponseSuccess()
        coEveryMovieResponse(requestType = ReturnType.SUCCESS, response)
        // Act
        val result = _dataSource.find(apiId = 1)
        // Assert
        response.body.shouldBeEqualTo(result.data)
    }

    @Test
    fun `should be instance of success when request type is success`() = runTest {
        // Arrange
        coEveryMovieResponse(requestType = ReturnType.SUCCESS)
        // Act
        val result = _dataSource.find(apiId = 1)
        // Assert
        result.shouldBeInstanceOf<DataResult.Success<Movie>>()
    }

    @Test
    fun `should have a message error when result is serve error`() = runTest {
        // Arrange
        coEveryMovieResponse(requestType = ReturnType.SERVER_ERROR)
        // Act
        val result = _dataSource.find(apiId = 1)
        // Assert
        ERROR_MSG.shouldBeEqualTo(result.message)
    }

    @Test
    fun `should be instance of serve error when request type is serve error`() = runTest {
        // Arrange
        coEveryMovieResponse(requestType = ReturnType.SERVER_ERROR)
        // Act
        val result = _dataSource.find(apiId = 1)
        // Assert
        result.shouldBeInstanceOf<DataResult.ServerError<Movie>>()
    }

    @Test
    fun `should be instance of network error when request type is network error`() = runTest {
        // Arrange
        coEveryMovieResponse(requestType = ReturnType.NETWORK_ERROR)
        // Act
        val result = _dataSource.find(apiId = 1)
        // Assert
        result.shouldBeInstanceOf<DataResult.NetworkError<Movie>>()
    }

    @Test
    fun `should be instance of unknown error when request type is unknown error`() = runTest {
        // Arrange
        coEveryMovieResponse(requestType = ReturnType.UNKNOWN_ERROR)
        // Act
        val result = _dataSource.find(apiId = 1)
        // Assert
        result.shouldBeInstanceOf<DataResult.UnknownError<Movie>>()
    }

    private fun coEveryMovieResponse(
        requestType: ReturnType,
        successResponse: MovieSuccess = createMovieResponseSuccess()
    ) = coEvery {
        _api.getMovie(id = any(), language = any(), region = any())
    } returns mockResponse(requestType, successResponse)

    private fun createMovieResponseSuccess() =
        NetworkResponse.Success(body = Movie(), code = 200)
}
