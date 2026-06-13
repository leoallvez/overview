package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.domain.repository.GetPage
import br.dev.singular.overview.domain.repository.Page
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.createMediaMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
    fun `invoke with MOVIE type should fetch only movies with correct key`() = runTest {
        // arrange
        val query = QueryState(type = MediaType.MOVIE, page = 1)
        val expectedPage = Page(items = listOf(createMediaMock(type = MediaType.MOVIE)), currentPage = 1)

        coEvery {
            getter.getPage(match { it.type == MediaType.MOVIE && it.key == "test_movie" && it.page == 1 })
        } returns expectedPage

        // act
        val result = sut.invoke(query)

        // assert
        assertTrue(result is UseCaseState.Success)
        val actualPage = (result as UseCaseState.Success).data
        assertEquals(expectedPage, actualPage)
        assertEquals(MediaType.MOVIE, actualPage.items.first().type)
        coVerify(exactly = 1) { getter.getPage(any()) }
    }

    @Test
    fun `invoke with TV type should fetch only tv shows with correct key`() = runTest {
        // arrange
        val query = QueryState(type = MediaType.TV, page = 2)
        val expectedPage = Page(items = listOf(createMediaMock(type = MediaType.TV)), currentPage = 2)

        coEvery {
            getter.getPage(match { it.type == MediaType.TV && it.key == "test_tv" && it.page == 2 })
        } returns expectedPage

        // act
        val result = sut.invoke(query)

        // assert
        assertTrue(result is UseCaseState.Success)
        assertEquals(expectedPage, (result as UseCaseState.Success).data)
        coVerify(exactly = 1) { getter.getPage(any()) }
    }

    @Test
    fun `invoke with ALL type should fetch and combine types correctly`() = runTest {
        // arrange
        val query = QueryState(type = MediaType.ALL, page = 1)
        val moviePage = Page(items = listOf(createMediaMock(type = MediaType.MOVIE)), currentPage = 1, isLastPage = false)
        val tvPage = Page(items = listOf(createMediaMock(type = MediaType.TV)), currentPage = 1, isLastPage = true)

        coEvery { getter.getPage(match { it.key == "test_movie" }) } returns moviePage
        coEvery { getter.getPage(match { it.key == "test_tv" }) } returns tvPage

        // act
        val result = sut.invoke(query)

        // assert
        assertTrue(result is UseCaseState.Success)
        val combinedPage = (result as UseCaseState.Success).data
        assertEquals(2, combinedPage.items.size)
        assertEquals(1, combinedPage.currentPage)
        assertTrue(combinedPage.isLastPage) // false || true = true
        
        coVerify(exactly = 1) { getter.getPage(match { it.key == "test_movie" }) }
        coVerify(exactly = 1) { getter.getPage(match { it.key == "test_tv" }) }
    }

    @Test
    fun `invoke should return empty page for UNKNOWN type`() = runTest {
        // arrange
        val query = QueryState(type = MediaType.UNKNOWN)

        // act
        val result = sut.invoke(query)

        // assert
        assertTrue(result is UseCaseState.Success)
        val page = (result as UseCaseState.Success).data
        assertTrue(page.items.isEmpty())
        assertFalse(page.isLastPage)
        assertEquals(0, page.currentPage)
        coVerify(exactly = 0) { getter.getPage(any()) }
    }

    @Test
    fun `invoke should return failure when repository throws exception`() = runTest {
        // arrange
        val query = QueryState(type = MediaType.MOVIE)
        val expectedException = RuntimeException("Network Error")
        coEvery { getter.getPage(any()) } throws expectedException

        // act
        val result = sut.invoke(query)

        // assert
        assertTrue(result is UseCaseState.Failure)
        val failure = result as UseCaseState.Failure
        assertEquals(expectedException, (failure.type as FailType.Exception).throwable)
        coVerify(exactly = 1) { getter.getPage(any()) }
    }
}
