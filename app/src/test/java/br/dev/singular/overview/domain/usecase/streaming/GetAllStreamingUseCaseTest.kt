package br.dev.singular.overview.domain.usecase.streaming

import br.dev.singular.overview.domain.model.Streaming
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.usecase.createStreamingMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetAllStreamingUseCaseTest {
    private lateinit var getter: GetAll<Streaming>
    private lateinit var sut: IGetAllStreamingUseCase
    private lateinit var streamingMock: Streaming

    @Before
    fun setup() {
        getter = mockk()
        sut = GetAllStreamingUseCase(getter)
        streamingMock = createStreamingMock()
    }

    @Test
    fun `invoke should return only streaming where display is true`() = runBlocking {
        // arrange
        val list = listOf(
            streamingMock.copy(id = 1, display = true),
            streamingMock.copy(id = 2, display = false),
            streamingMock.copy(id = 3, display = true)
        )
        coEvery { getter.getAll() } returns list

        // act
        val result = sut.invoke()

        // assert
        coVerify(exactly = 1) { getter.getAll() }
        assertEquals(2, result.size)
        assertTrue(result.all { it.display })
    }

    @Test
    fun `invoke should return empty list when no streaming has display true`() = runBlocking {
        // arrange
        val list = listOf(
            streamingMock.copy(display = false),
            streamingMock.copy(display = false)
        )
        coEvery { getter.getAll() } returns list

        // act
        val result = sut.invoke()

        // assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `invoke should return empty list when repository throws exception`() = runBlocking {
        // arrange
        coEvery { getter.getAll() } throws Exception("Network Error")

        // act
        val result = sut.invoke()

        // assert
        coVerify(exactly = 1) { getter.getAll() }
        assertTrue(result.isEmpty())
    }

    @Test
    fun `invoke should return empty list when repository returns empty`() = runBlocking {
        // arrange
        coEvery { getter.getAll() } returns emptyList()

        // act
        val result = sut.invoke()

        // assert
        assertTrue(result.isEmpty())
    }
}
