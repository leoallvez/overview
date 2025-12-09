package br.dev.singular.overview.domain.usecase.streaming

import br.dev.singular.overview.domain.model.Streaming
import br.dev.singular.overview.domain.repository.Update
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.createStreamingMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SaveSelectedStreamingUseCaseTest {

    private lateinit var updater: Update<Streaming>
    private lateinit var sut: ISaveSelectedStreamingUseCase
    private lateinit var streamingMock: Streaming

    @Before
    fun setup() {
        updater = mockk()
        sut = SaveSelectedStreamingUseCase(updater)
        streamingMock = createStreamingMock()
    }

    @Test
    fun `invoke should return Success when updater completes successfully`() = runBlocking {
        // arrange
        coEvery { updater.update(any()) } returns Unit

        // act
        val result = sut.invoke(streamingMock)

        // assert
        coVerify(exactly = 1) { updater.update(streamingMock) }
        assertTrue(result is UseCaseState.Success)
    }

    @Test
    fun `invoke should return Failure when updater throws exception`() = runBlocking {
        // arrange
        val exception = Exception("Database error")
        coEvery { updater.update(any()) } throws exception

        // act
        val result = sut.invoke(streamingMock)

        // assert
        coVerify(exactly = 1) { updater.update(streamingMock) }
        assertTrue(result is UseCaseState.Failure)
    }

    @Test
    fun `invoke should call updater with correct parameters`() = runBlocking {
        // arrange
        coEvery { updater.update(any()) } returns Unit

        // act
        sut.invoke(streamingMock)

        // assert
        coVerify { updater.update(streamingMock) }
    }
}
