package br.com.deepbyte.overview.data.source.genre

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.response.GenreListResponse
import br.com.deepbyte.overview.data.model.person.Person
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum.ALL
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

private typealias GenreSuccess = NetworkResponse.Success<GenreListResponse>

class GenreRemoteDataSourceTest {

    @MockK(relaxed = true)
    private lateinit var _api: ApiService
    private lateinit var _dataSource: IGenreRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        _dataSource = GenreRemoteDataSource(_api, _locale = mockk(relaxed = true))
    }

    @Test
    fun `should be equals response and result when call get item`() = runTest {
        // Arrange
        val response = createGenreResponseSuccess()
        coEveryGenreResponse(requestType = ReturnType.SUCCESS, response)
        // Act
        val result = _dataSource.getItemByMediaType(type = ALL)
        // Assert
        response.body.shouldBeEqualTo(result.data)
    }

    @Test
    fun `should be instance of success when request type is success`() = runTest {
        // Arrange
        coEveryGenreResponse(requestType = ReturnType.SUCCESS)
        // Act
        val result = _dataSource.getItemByMediaType(type = ALL)
        // Assert
        result.shouldBeInstanceOf<DataResult.Success<GenreListResponse>>()
    }

    @Test
    fun `should have a message error when result is serve error`() = runTest {
        // Arrange
        coEveryGenreResponse(requestType = ReturnType.SERVER_ERROR)
        // Act
        val result = _dataSource.getItemByMediaType(type = ALL)
        // Assert
        ERROR_MSG.shouldBeEqualTo(result.message)
    }

    @Test
    fun `should be instance of serve error when request type is serve error`() = runTest {
        // Arrange
        coEveryGenreResponse(requestType = ReturnType.SERVER_ERROR)
        // Act
        val result = _dataSource.getItemByMediaType(type = ALL)
        // Assert
        result.shouldBeInstanceOf<DataResult.ServerError<GenreListResponse>>()
    }

    @Test
    fun `should be instance of network error when request type is network error`() = runTest {
        // Arrange
        coEveryGenreResponse(requestType = ReturnType.NETWORK_ERROR)
        // Act
        val result = _dataSource.getItemByMediaType(type = ALL)
        // Assert
        result.shouldBeInstanceOf<DataResult.NetworkError<GenreListResponse>>()
    }

    @Test
    fun `should be instance of unknown error when request type is unknown error`() = runTest {
        // Arrange
        coEveryGenreResponse(requestType = ReturnType.UNKNOWN_ERROR)
        // Act
        val result = _dataSource.getItemByMediaType(type = ALL)
        // Assert
        result.shouldBeInstanceOf<DataResult.UnknownError<GenreListResponse>>()
    }

    private fun coEveryGenreResponse(
        requestType: ReturnType,
        successResponse: GenreSuccess = createGenreResponseSuccess()
    ) = coEvery {
        _api.getGenreByMediaType(any(), any(), any())
    } returns mockResponse(requestType, successResponse)

    private fun createGenreResponseSuccess() =
        NetworkResponse.Success(body = GenreListResponse(), code = 200)
}
