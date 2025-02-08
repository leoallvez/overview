package br.dev.singular.overview.data.repository

import br.dev.singular.overview.data.network.response.ListResponse
import br.dev.singular.overview.data.network.source.DataResult
import br.dev.singular.overview.data.network.source.IMediaRemoteDataSource
import br.dev.singular.overview.data.util.mediaDataModel
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.repository.IMediaRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.amshove.kluent.internal.assertFailsWith
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MediaRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var remoteSource: IMediaRemoteDataSource

    private lateinit var sut: IMediaRepository

    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = MediaRepository(remoteSource)
    }

    @Test
    fun `should return list of media when response is success`() = runTest(dispatcher) {
        // Arrange
        coEvery { remoteSource.getByPath("some-path") } returns DataResult.Success(
            ListResponse(results = listOf(mediaDataModel))
        )

        // Act
        val result: List<Media> = sut.getByPath("some-path")

        // Assert
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `should throw RepositoryException when response is error`() = runTest(dispatcher) {
        // Arrange
        val errorMessage = "Failed to fetch data"
        coEvery { remoteSource.getByPath("some-path") } returns DataResult.Error(errorMessage)

        // Act & Assert
        val exception = assertFailsWith<RepositoryException> {
            sut.getByPath("some-path")
        }
        assertEquals(errorMessage, exception.message)
    }
}
