package br.dev.singular.overview.data.source.streaming

import br.dev.singular.overview.data.api.ApiService
import br.dev.singular.overview.data.api.response.ErrorResponse
import br.dev.singular.overview.data.api.response.ListResponse
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.util.mock.ERROR_MSG
import com.haroldadmin.cnradapter.NetworkResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldNotBeEmpty
import org.junit.Before
import org.junit.Test
import java.io.IOException

class StreamingRemoteDataSourceTestConfig {

    @MockK(relaxed = true)
    private lateinit var _api: ApiService
    private lateinit var _dataSource: IStreamingRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        _dataSource = StreamingRemoteDataSource(_api, _locale = mockk(relaxed = true))
    }

    @Test
    fun `should be empty result when remote data source is a success with empty result`() =
        runTest {
            // Arrange
            val body = ListResponse<StreamingEntity>(results = listOf())
            coEvery {
                _api.getStreamingItems()
            } returns NetworkResponse.Success(body = body, code = 200)
            // Act
            val result = _dataSource.getItems()
            // Assert
            result.shouldBeEmpty()
        }

    @Test
    fun `should not be empty result when remote data source is a success with not empty result`() =
        runTest {
            // Arrange
            val body = ListResponse(results = listOf(StreamingEntity()))
            coEvery {
                _api.getStreamingItems()
            } returns NetworkResponse.Success(body = body, code = 200)
            // Act
            val result = _dataSource.getItems()
            // Assert
            result.shouldNotBeEmpty()
        }

    @Test
    fun `should not be empty result when remote data source is a network error`() = runTest {
        // Arrange
        coEvery {
            _api.getStreamingItems()
        } returns NetworkResponse.NetworkError(IOException("Exception"))
        // Act
        val result = _dataSource.getItems()
        // Assert
        result.shouldBeEmpty()
    }

    @Test
    fun `should not be empty result when remote data source is a server error`() = runTest {
        // Arrange
        val body = ErrorResponse(success = false, code = 500, message = ERROR_MSG)
        coEvery {
            _api.getStreamingItems()
        } returns NetworkResponse.ServerError(body = body, code = 500)
        // Act
        val result = _dataSource.getItems()
        // Assert
        result.shouldBeEmpty()
    }

    @Test
    fun `should not be empty result when remote data source is a unknown error`() = runTest {
        // Arrange
        coEvery {
            _api.getStreamingItems()
        } returns NetworkResponse.UnknownError(Exception("exception"))
        // Act
        val result = _dataSource.getItems()
        // Assert
        result.shouldBeEmpty()
    }
}
