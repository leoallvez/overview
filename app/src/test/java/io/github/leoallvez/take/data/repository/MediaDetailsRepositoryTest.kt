package io.github.leoallvez.take.data.repository

import br.com.deepbyte.take.data.api.response.MediaDetailResponse
import br.com.deepbyte.take.data.api.response.ProviderResponse
import br.com.deepbyte.take.data.repository.MediaDetailsRepository
import br.com.deepbyte.take.data.source.DataResult
import br.com.deepbyte.take.data.source.DataResult.NetworkError
import br.com.deepbyte.take.data.source.DataResult.UnknownError
import br.com.deepbyte.take.data.source.media_item.IMediaRemoteDataSource
import br.com.deepbyte.take.data.source.provider.IProviderRemoteDataSource
import io.github.leoallvez.take.util.mock.ReturnType
import io.github.leoallvez.take.util.mock.mockResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MediaDetailsRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var _mediaSource: IMediaRemoteDataSource

    @MockK(relaxed = true)
    private lateinit var _providerSource: IProviderRemoteDataSource

    private lateinit var _repository: MediaDetailsRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before fun setup() {
        MockKAnnotations.init(this)
        val dispatcher = UnconfinedTestDispatcher()
        _repository = MediaDetailsRepository(_mediaSource, _providerSource, dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_success() = runTest {
        // Arrange
        coEveryProviderSuccessResponse()
        coEveryMediaDetailResponse(requestType = ReturnType.SUCCESS)
        // Act
        val result = _repository.getMediaDetailsResult(apiId = ID, mediaType = TYPE).first()
        // Assert
        assertTrue(result is DataResult.Success)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_serverError() = runTest {
        // Arrange
        coEveryMediaDetailResponse(requestType = ReturnType.SERVER_ERROR)
        // Act
        val result = _repository.getMediaDetailsResult(apiId = ID, mediaType = TYPE).first()
        // Assert
        assertTrue(result is DataResult.ServerError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_networkError() = runTest {
        // Arrange
        coEveryMediaDetailResponse(requestType = ReturnType.NETWORK_ERROR)
        // Act
        val result = _repository.getMediaDetailsResult(apiId = ID, mediaType = TYPE).first()
        // Assert
        assertTrue(result is NetworkError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_unknownError() = runTest {
        // Arrange
        coEveryMediaDetailResponse(requestType = ReturnType.UNKNOWN_ERROR)
        // Act
        val result = _repository.getMediaDetailsResult(apiId = ID, mediaType = TYPE).first()
        // Assert
        assertTrue(result is UnknownError)
    }

    private fun coEveryMediaDetailResponse(requestType: ReturnType) = coEvery {
        _mediaSource.getMediaDetailsResult(any(), any())
    } returns createMediaDetailResponse(requestType)

    private fun createMediaDetailResponse(
        requestType: ReturnType
    ) = mockResult(requestType, DataResult.Success(data = MediaDetailResponse()))

    private fun coEveryProviderSuccessResponse() = coEvery {
        _providerSource.getProvidersResult(any(), any())
    } returns mockResult(ReturnType.SUCCESS, DataResult.Success(data = ProviderResponse()))

    private companion object {
        const val ID = 1L
        const val TYPE = "movie"
    }
}
