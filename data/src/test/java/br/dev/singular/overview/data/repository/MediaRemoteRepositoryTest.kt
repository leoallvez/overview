package br.dev.singular.overview.data.repository

import br.dev.singular.overview.data.local.source.IMediaRouteLocalDataSource
import br.dev.singular.overview.data.model.MediaDataPage
import br.dev.singular.overview.data.model.MediaRouteDataModel
import br.dev.singular.overview.data.network.source.DataResult
import br.dev.singular.overview.data.network.source.IMediaRemoteDataSource
import br.dev.singular.overview.data.util.fakeMediaDataModel
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.domain.repository.GetPage
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MediaRemoteRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var remoteSource: IMediaRemoteDataSource

    @MockK(relaxed = true)
    private lateinit var routeSource: IMediaRouteLocalDataSource

    private lateinit var sut: GetPage<Media, QueryState>

    private val dispatcher = UnconfinedTestDispatcher()

    private val queryState = QueryState(key = "all_trending")

    private val routeData = MediaRouteDataModel(
        key = "all_trending",
        path = "trending/all/day",
        params = mapOf("with_watch_providers" to "8")
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = MediaRemoteRepository(
            remoteSource,
            routeSource
        )
    }

    @Test
    fun `should return list of media when response is success`() = runTest(dispatcher) {
        // Arrange
        coEvery { routeSource.getByKey(any()) } returns routeData
        coEvery { remoteSource.getByQuery(any(), any()) } returns DataResult.Success(
            MediaDataPage(items = listOf(fakeMediaDataModel))
        )

        // Act
        val result: List<Media> = sut.getPage(queryState).items

        // Assert
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `should return a empty list when response is error`() = runTest(dispatcher) {
        // Arrange
        coEvery { routeSource.getByKey(any()) } returns routeData
        coEvery { remoteSource.getByQuery(any(), any()) } returns DataResult.Error()

        // Act
        val result = sut.getPage(queryState).items

        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `should return a empty list when it fails to get the route`() = runTest(dispatcher) {
        // Arrange
        coEvery { routeSource.getByKey(any()) } returns null

        // Act
        val result = sut.getPage(QueryState(key = "invalid_key")).items

        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `should pass extra params from route to remote source`() = runTest(dispatcher) {
        // Arrange
        coEvery { routeSource.getByKey("all_trending") } returns routeData
        coEvery { remoteSource.getByQuery(any(), any()) } returns DataResult.Success(
            MediaDataPage(items = listOf(fakeMediaDataModel))
        )

        // Act
        sut.getPage(queryState)

        // Assert
        coVerify {
            remoteSource.getByQuery(
                queryState = any(),
                extraParams = routeData.params
            )
        }
    }
}
