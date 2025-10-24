package br.dev.singular.overview.data.repository

import br.dev.singular.overview.data.local.source.ISuggestionLocalDataSource
import br.dev.singular.overview.data.network.source.ISuggestionRemoteDataSource
import br.dev.singular.overview.data.util.mappers.toDomain
import br.dev.singular.overview.data.util.suggestionModels
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SuggestionRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var localDataSource: ISuggestionLocalDataSource

    @MockK(relaxed = true)
    private lateinit var remoteDataSource: ISuggestionRemoteDataSource

    private lateinit var sut: SuggestionRepository

    private val domainSuggestions = suggestionModels.map { it.toDomain() }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = SuggestionRepository(localDataSource, remoteDataSource)
    }

    @Test
    fun `should return local suggestions when local is not empty`() = runTest {
        // Arrange
        coEvery { localDataSource.getAll() } returns suggestionModels

        // Act
        val result = sut.getAll()

        // Assert
        assertEquals(domainSuggestions, result)
        coVerify(exactly = 0) { remoteDataSource.getAll() }
    }

    @Test
    fun `should fetch from remote and save cache when local is empty`() = runTest {
        // Arrange
        coEvery{ localDataSource.getAll() } returns emptyList()
        coEvery { remoteDataSource.getAll() } returns suggestionModels

        // Act
        val result = sut.getAll()

        // Assert
        assertEquals(domainSuggestions, result)
        coVerify(exactly = 1) { remoteDataSource.getAll() }
        coVerify(exactly = 1) { localDataSource.insert(suggestionModels) }
    }

    @Test
    fun `should return empty list when local is empty and remote throws exception`() = runTest {
        // Arrange
        coEvery { localDataSource.getAll() } returns emptyList()
        coEvery { remoteDataSource.getAll() } throws RuntimeException("Remote failure")

        // Act
        val result = sut.getAll()

        // Assert
        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { remoteDataSource.getAll() }
    }

    @Test
    fun `should call deleteAll on local data source`() = runTest {
        // Arrange
        coJustRun { localDataSource.deleteAll() }

        // Act
        sut.deleteAll()

        // Assert
        coVerify(exactly = 1) { localDataSource.deleteAll() }
    }
}
