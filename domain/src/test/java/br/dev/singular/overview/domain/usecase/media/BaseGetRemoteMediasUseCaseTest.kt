package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.domain.repository.GetPage
import br.dev.singular.overview.domain.repository.Page
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.createMediaMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BaseGetRemoteMediasUseCaseTest {

    private lateinit var sut: IGetRemoteMediasUseCase
    private val getter: GetPage<Media, QueryState> = mockk()
    private val keyPrefix = "test"

    @Before
    fun setup() {
        sut = BaseGetRemoteMediasUseCase(keyPrefix, getter)
    }

    @After
    fun tearDown() {
        confirmVerified(getter)
    }

    @Test
    fun `invoke with MOVIE type should fetch only movies`() = runBlocking {
        // arrange
        val query = QueryState(type = MediaType.MOVIE)
        val expectedPage = Page(items = listOf(createMediaMock(type = MediaType.MOVIE)))

        coEvery {
            getter.getPage(match { it.type == MediaType.MOVIE && it.key == "${keyPrefix}_movie" })
        } returns expectedPage

        // act
        val result = sut.invoke(query)

        // assert
        assertTrue(result is UseCaseState.Success)
        assertEquals(expectedPage, (result as UseCaseState.Success).data)
        coVerify(exactly = 1) { getter.getPage(any()) }
    }

    @Test
    fun `invoke with TV type should fetch only tv shows`() = runBlocking {
        // arrange
        val query = QueryState(type = MediaType.TV)
        val expectedPage = Page(items = listOf(createMediaMock(type = MediaType.TV)))

        coEvery {
            getter.getPage(match { it.type == MediaType.TV && it.key == "${keyPrefix}_tv" })
        } returns expectedPage

        // act
        val result = sut.invoke(query)

        // assert
        assertTrue(result is UseCaseState.Success)
        assertEquals(expectedPage, (result as UseCaseState.Success).data)
        coVerify(exactly = 1) { getter.getPage(any()) }
    }

    @Test
    fun `invoke with ALL type should fetch and combine movies and tv shows`() = runBlocking {
        // arrange
        val query = QueryState(type = MediaType.ALL)
        val moviePage = Page(items = listOf(createMediaMock(type = MediaType.MOVIE)))
        val tvPage = Page(items = listOf(createMediaMock(type = MediaType.TV)))

        coEvery {
            getter.getPage(match { it.key == "${keyPrefix}_movie" })
        } returns moviePage

        coEvery {
            getter.getPage(match { it.key == "${keyPrefix}_tv" })
        } returns tvPage

        // act
        val result = sut.invoke(query)

        // assert
        assertTrue(result is UseCaseState.Success)
        val combinedPage = (result as UseCaseState.Success).data
        assertEquals(2, combinedPage.items.size)
        coVerify(exactly = 2) { getter.getPage(any()) }
    }

    @Test
    fun `invoke should return empty page for UNKNOWN type`() = runBlocking {
        // arrange
        val query = QueryState(type = MediaType.UNKNOWN)

        // act
        val result = sut.invoke(query)

        // assert
        assertTrue(result is UseCaseState.Success)
        assertTrue((result as UseCaseState.Success).data.items.isEmpty())
        coVerify(exactly = 0) { getter.getPage(any()) }
    }

    @Test
    fun `invoke should return failure when repository throws`() = runBlocking {
        // arrange
        val query = QueryState(type = MediaType.MOVIE)
        coEvery { getter.getPage(any()) } throws Exception("Network error")

        // act
        val result = sut.invoke(query)

        // assert
        assertTrue(result is UseCaseState.Failure)
        coVerify(exactly = 1) { getter.getPage(any()) }
    }
}
