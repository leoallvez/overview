package br.com.deepbyte.overview.data.repository.streaming

import br.com.deepbyte.overview.data.model.provider.StreamingEntity
import br.com.deepbyte.overview.data.source.streaming.StreamingLocalDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldNotBeEmpty
import org.junit.Before
import org.junit.Test

class StreamingRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var _localSource: StreamingLocalDataSource

    private lateinit var _repository: StreamingRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockKAnnotations.init(this)
        val dispatcher = UnconfinedTestDispatcher()
        _repository = StreamingRepository(_localSource, dispatcher)
    }

    @Test
    fun `should be empty result when local data source is empty`() = runTest {
        // Arrange
        coEvery { _localSource.getItems() } returns listOf()
        // Act
        val result = _repository.getItems().first()
        // Assert
        result.shouldBeEmpty()
    }

    @Test
    fun `should not be empty result when local data source is not empty`() = runTest {
        // Arrange
        coEvery { _localSource.getItems() } returns listOf(StreamingEntity())
        // Act
        val result = _repository.getItems().first()
        // Assert
        result.shouldNotBeEmpty()
    }
}
