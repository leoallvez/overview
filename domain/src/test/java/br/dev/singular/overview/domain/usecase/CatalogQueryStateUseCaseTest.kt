package br.dev.singular.overview.domain.usecase

import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.domain.repository.Get
import br.dev.singular.overview.domain.repository.Observe
import br.dev.singular.overview.domain.repository.Update
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class CatalogQueryStateUseCaseTest {

    private lateinit var sut: ICatalogQueryStateUseCase
    private val getter: Get<QueryState?> = mockk()
    private val updater: Update<QueryState?> = mockk()
    private val observer: Observe<QueryState?> = mockk()

    @Before
    fun setup() {
        sut = CatalogQueryStateUseCase(getter, updater, observer)
    }

    @After
    fun tearDown() {
        confirmVerified(getter, updater, observer)
    }

    @Test
    fun `observe should return flow from repository`() = runBlocking {
        // arrange
        val expected = QueryState(query = "observed")
        every { observer.observe() } returns flowOf(expected)

        // act
        val result = sut.observe().first()

        // assert
        assertEquals(expected, result)
        verify(exactly = 1) { observer.observe() }
    }

    @Test
    fun `get should return state from repository`() = runBlocking {
        // arrange
        val expected = QueryState(query = "test")
        coEvery { getter.get() } returns expected

        // act
        val result = sut.get()

        // assert
        assertEquals(expected, result)
        coVerify(exactly = 1) { getter.get() }
    }

    @Test
    fun `save should call repository with state`() = runBlocking {
        // arrange
        val state = QueryState(query = "new search")
        coEvery { updater.update(state) } returns Unit

        // act
        sut.save(state)

        // assert
        coVerify(exactly = 1) { updater.update(state) }
    }

    @Test
    fun `get should propagate exception when repository fails`() = runBlocking {
        // arrange
        val message = "DataStore error"
        coEvery { getter.get() } throws Exception(message)

        // act
        try {
            sut.get()
            fail("Should have thrown")
        } catch (e: Exception) {
            // assert
            assertEquals(message, e.message)
            coVerify(exactly = 1) { getter.get() }
        }
    }

    @Test
    fun `save should propagate exception when repository fails`() = runBlocking {
        // arrange
        val state = QueryState()
        val message = "DataStore error"
        coEvery { updater.update(state) } throws Exception(message)

        // act
        try {
            sut.save(state)
            fail("Should have thrown")
        } catch (e: Exception) {
            // assert
            assertEquals(message, e.message)
            coVerify(exactly = 1) { updater.update(state) }
        }
    }
}
