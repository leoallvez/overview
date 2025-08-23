package br.dev.singular.overview.data.repository

import br.dev.singular.overview.data.network.response.ListResponse
import br.dev.singular.overview.data.network.source.DataResult
import br.dev.singular.overview.data.network.source.IMediaRemoteDataSource
import br.dev.singular.overview.data.util.MockMediaRouteLocalDataSource
import br.dev.singular.overview.data.util.mediaDataModel
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaParam
import br.dev.singular.overview.domain.repository.GetAllByParam
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MediaRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var remoteSource: IMediaRemoteDataSource

    private lateinit var sut: GetAllByParam<Media, MediaParam>

    private val dispatcher = UnconfinedTestDispatcher()

    private val param = MediaParam(key = "all_trending")

    private val validPath: String = "trending/all/day"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = MediaRepository(
            remoteSource,
            MockMediaRouteLocalDataSource()
        )
    }

    @Test
    fun `should return list of media when response is success`() = runTest(dispatcher) {
        // Arrange
        coEvery { remoteSource.getByPath(validPath) } returns DataResult.Success(
            ListResponse(results = listOf(mediaDataModel))
        )

        // Act
        val result: List<Media> = sut.getAllByParam(param)

        // Assert
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `should return a empty list when response is error`() = runTest(dispatcher) {
        // Arrange
        val errorMessage = "Failed to fetch data"
        coEvery { remoteSource.getByPath(validPath) } returns DataResult.Error(errorMessage)

        // Act
        val result = sut.getAllByParam(param)

        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `should return a empty list when it fails to get the route` () = runTest(dispatcher) {
        // Arrange
        coEvery { remoteSource.getByPath(validPath) } returns DataResult.Success(
            ListResponse(results = listOf(mediaDataModel))
        )

        // Act
        val result = sut.getAllByParam(MediaParam(key = "invalid_key"))

        // Assert
        assertTrue(result.isEmpty())

    }
}
