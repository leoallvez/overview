package br.dev.singular.overview.data.source.media.remote

import br.dev.singular.overview.data.api.ApiService
import br.dev.singular.overview.data.model.media.TvShow
import br.dev.singular.overview.data.source.DataResult
import br.dev.singular.overview.util.mock.ERROR_MSG
import br.dev.singular.overview.util.mock.ReturnType
import br.dev.singular.overview.util.mock.mockResponse
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

private typealias TvShowSuccess = NetworkResponse.Success<TvShow>

class TvShowRemoteDataSourceTestConfig {

    @MockK(relaxed = true)
    private lateinit var _api: ApiService
    private lateinit var _dataSource: IMediaRemoteDataSource<TvShow>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        _dataSource = TvShowRemoteDataSource(_api, _locale = mockk(relaxed = true))
    }

    @Test
    fun `should be equals response and result when call get item`() = runTest {
        // Arrange
        val response = createTvShowResponseSuccess()
        coEveryTvShowResponse(requestType = ReturnType.SUCCESS, response)
        // Act
        val result = _dataSource.find(apiId = 1)
        // Assert
        response.body.shouldBeEqualTo(result.data)
    }

    @Test
    fun `should be instance of success when request type is success`() = runTest {
        // Arrange
        coEveryTvShowResponse(requestType = ReturnType.SUCCESS)
        // Act
        val result = _dataSource.find(apiId = 1)
        // Assert
        result.shouldBeInstanceOf<DataResult.Success<TvShow>>()
    }

    @Test
    fun `should have a message error when result is serve error`() = runTest {
        // Arrange
        coEveryTvShowResponse(requestType = ReturnType.SERVER_ERROR)
        // Act
        val result = _dataSource.find(apiId = 1)
        // Assert
        ERROR_MSG.shouldBeEqualTo(result.message)
    }

    @Test
    fun `should be instance of serve error when request type is serve error`() = runTest {
        // Arrange
        coEveryTvShowResponse(requestType = ReturnType.SERVER_ERROR)
        // Act
        val result = _dataSource.find(apiId = 1)
        // Assert
        result.shouldBeInstanceOf<DataResult.ServerError<TvShow>>()
    }

    @Test
    fun `should be instance of network error when request type is network error`() = runTest {
        // Arrange
        coEveryTvShowResponse(requestType = ReturnType.NETWORK_ERROR)
        // Act
        val result = _dataSource.find(apiId = 1)
        // Assert
        result.shouldBeInstanceOf<DataResult.NetworkError<TvShow>>()
    }

    @Test
    fun `should be instance of unknown error when request type is unknown error`() = runTest {
        // Arrange
        coEveryTvShowResponse(requestType = ReturnType.UNKNOWN_ERROR)
        // Act
        val result = _dataSource.find(apiId = 1)
        // Assert
        result.shouldBeInstanceOf<DataResult.UnknownError<TvShow>>()
    }

    private fun coEveryTvShowResponse(
        requestType: ReturnType,
        successResponse: TvShowSuccess = createTvShowResponseSuccess()
    ) = coEvery {
        _api.getTvShow(id = any(), language = any(), region = any())
    } returns mockResponse(requestType, successResponse)

    private fun createTvShowResponseSuccess() =
        NetworkResponse.Success(body = TvShow(), code = 200)
}
