package io.github.leoallvez.take.data.repository

import io.github.leoallvez.take.data.api.response.MediaDetailResponse
import io.github.leoallvez.take.data.api.response.ProviderResponse
import io.github.leoallvez.take.data.source.DataResult.*
import io.github.leoallvez.take.data.source.media_item.IMediaRemoteDataSource
import io.github.leoallvez.take.data.source.provider.IProviderRemoteDataSource
import io.github.leoallvez.take.util.mock.ReturnType
import io.github.leoallvez.take.util.mock.ReturnType.*
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
        _repository = MediaDetailsRepository(
            _mediaSource,
            _providerDataSource = _providerSource,
            UnconfinedTestDispatcher()
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_success() = runTest {
        //Arrange
        coEvery { _providerSource.getProvidersResult(any(), any()) } returns
                Success(data = ProviderResponse())

        coEveryMediaDetailResponse(requestType = SUCCESS)
        //Act
        val result = _repository.getMediaDetailsResult(apiId = ID, mediaType = TYPE).first()
        //Assert
        assertTrue(result is Success)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_serverError() = runTest {
        //Arrange
        coEveryMediaDetailResponse(requestType = SERVER_ERROR)
        //Act
        val result = _repository.getMediaDetailsResult(apiId = ID, mediaType = TYPE).first()
        //Assert
        assertTrue(result is ServerError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_networkError() = runTest {
        //Arrange
        coEveryMediaDetailResponse(requestType = NETWORK_ERROR)
        //Act
        val result = _repository.getMediaDetailsResult(apiId = ID, mediaType = TYPE).first()
        //Assert
        assertTrue(result is NetworkError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_unknownError() = runTest {
        //Arrange
        coEveryMediaDetailResponse(requestType = UNKNOWN_ERROR)
        //Act
        val result = _repository.getMediaDetailsResult(apiId = ID, mediaType = TYPE).first()
        //Assert
        assertTrue(result is UnknownError)
    }

    private fun coEveryMediaDetailResponse(requestType: ReturnType) = coEvery {
        _mediaSource.getMediaDetailsResult(any(), any())
    } returns createMediaDetailResponse(requestType)

    private fun createMediaDetailResponse(requestType: ReturnType) =
        mockResult(requestType, Success(data = MediaDetailResponse()))

    private companion object {
        const val ID = 1L
        const val TYPE = "movie"
    }
}
