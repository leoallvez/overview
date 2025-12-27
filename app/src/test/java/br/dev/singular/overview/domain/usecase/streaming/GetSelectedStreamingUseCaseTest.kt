package br.dev.singular.overview.domain.usecase.streaming

import br.dev.singular.overview.domain.model.Streaming
import br.dev.singular.overview.domain.repository.Get
import br.dev.singular.overview.domain.usecase.createStreamingMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GetSelectedStreamingUseCaseTest {

    private lateinit var getter: Get<Streaming?>
    private lateinit var sut: IGetSelectedStreamingUseCase
    private lateinit var streamingMock: Streaming

    @Before
    fun setup() {
        getter = mockk()
        sut = GetSelectedStreamingUseCase(getter)
        streamingMock = createStreamingMock().copy(name = "Disney+")
    }

    @Test
    fun `invoke should return streaming when getter returns data`() = runBlocking {
        // arrange
        coEvery { getter.get() } returns streamingMock

        // act
        val result = sut.invoke()

        // assert
        coVerify(exactly = 1) { getter.get() }
        assertEquals(streamingMock, result)
        assertEquals("Disney+", result?.name)
    }

    @Test
    fun `invoke should return null when getter returns null`() = runBlocking {
        // arrange
        coEvery { getter.get() } returns null

        // act
        val result = sut.invoke()

        // assert
        coVerify(exactly = 1) { getter.get() }
        assertNull(result)
    }

    @Test
    fun `invoke should return null when repository throws exception`() = runBlocking {
        // arrange
        coEvery { getter.get() } throws Exception("Database error")

        // act
        val result = sut.invoke()

        // assert
        coVerify(exactly = 1) { getter.get() }
        assertNull(result)
    }
}