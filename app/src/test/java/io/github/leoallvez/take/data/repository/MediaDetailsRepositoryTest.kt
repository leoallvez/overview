package io.github.leoallvez.take.data.repository

import io.github.leoallvez.take.data.api.response.MediaDetailResponse
import io.github.leoallvez.take.data.source.DataResult.*
import io.github.leoallvez.take.data.source.mediaitem.IMediaRemoteDataSource
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
    private lateinit var _dataSource: IMediaRemoteDataSource

    private lateinit var _repository: MediaDetailsRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before fun setup() {
        MockKAnnotations.init(this)
        _repository = MediaDetailsRepository(_dataSource, UnconfinedTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_success() = runTest {
        //Arrange
        mockResponse(requestType = "Success")
        //Act
        val result = _repository.getMediaDetailsResult(apiId = ID, mediaType = TYPE).first()
        //Assert
        assertTrue(result is Success)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_serverError() = runTest {
        //Arrange
        mockResponse(requestType = "ServerError")
        //Act
        val result = _repository.getMediaDetailsResult(apiId = ID, mediaType = TYPE).first()
        //Assert
        assertTrue(result is ServerError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_networkError() = runTest {
        //Arrange
        mockResponse(requestType = "NetworkError")
        //Act
        val result = _repository.getMediaDetailsResult(apiId = ID, mediaType = TYPE).first()
        //Assert
        assertTrue(result is NetworkError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun getMediaDetailsResult_unknownError() = runTest {
        //Arrange
        mockResponse(requestType = "UnknownError")
        //Act
        val result = _repository.getMediaDetailsResult(apiId = ID, mediaType = TYPE).first()
        //Assert
        assertTrue(result is UnknownError)
    }

    private fun mockResponse(requestType: String) = coEvery {
        _dataSource.getMediaDetailsResult(any(), any())
    } returns getResponseByType(requestType)

    private fun getResponseByType(requestType: String) = when(requestType) {
        "Success"      -> Success(data = MediaDetailResponse())
        "ServerError"  -> ServerError()
        "NetworkError" -> NetworkError()
        "UnknownError" -> UnknownError()
        else -> throw IllegalArgumentException()
    }

    private companion object {
        const val ID = 1L
        const val TYPE = "movie"
    }
}
