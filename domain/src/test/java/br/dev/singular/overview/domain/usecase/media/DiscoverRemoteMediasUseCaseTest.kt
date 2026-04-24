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
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DiscoverRemoteMediasUseCaseTest {

    private lateinit var sut: IGetRemoteMediasUseCase
    private lateinit var getter: GetPage<Media, QueryState>

    @Before
    fun setup() {
        getter = mockk()
        sut = DiscoverRemoteMediasUseCase(getter)
    }

    @Test
    fun `invoke with MOVIE type should fetch movies`() = runBlocking {
        // arrange
        val query = QueryState(type = MediaType.MOVIE)
        val expectedPage = Page(items = listOf(createMediaMock(type = MediaType.MOVIE)))
        coEvery { getter.getPage(any()) } returns expectedPage

        // act
        val result = sut.invoke(query)

        // assert
        coVerify(exactly = 1) {
            getter.getPage(match { it.type == MediaType.MOVIE })
        }
        assertTrue(result is UseCaseState.Success)
        assertEquals(expectedPage.items, (result as UseCaseState.Success).data.items)
    }

    @Test
    fun `invoke with TV type should fetch tv shows`() = runBlocking {
        // arrange
        val query = QueryState(type = MediaType.TV)
        val expectedPage = Page(items = listOf(createMediaMock(type = MediaType.TV)))
        coEvery { getter.getPage(any()) } returns expectedPage

        // act
        val result = sut.invoke(query)

        // assert
        coVerify(exactly = 1) {
            getter.getPage(match { it.type == MediaType.TV })
        }
        assertTrue(result is UseCaseState.Success)
        assertEquals(expectedPage.items, (result as UseCaseState.Success).data.items)
    }

    @Test
    fun `invoke with ALL type should fetch and combine MOVIE and TV`() = runBlocking {
        // arrange
        val query = QueryState(type = MediaType.ALL)

        coEvery {
            getter.getPage(match { it.key == "discover_movie" })
        } returns Page(items = listOf(createMediaMock(type = MediaType.MOVIE)))

        coEvery {
            getter.getPage(match { it.key == "discover_tv" })
        } returns Page(items = listOf(createMediaMock(type = MediaType.TV)))


        // act
        val result = sut.invoke(query)

        // assert
        coVerify(exactly = 1) { getter.getPage(match { it.key == "discover_movie" }) }
        coVerify(exactly = 1) { getter.getPage(match { it.key == "discover_tv" }) }

        assertTrue(result is UseCaseState.Success)
        val successData = (result as UseCaseState.Success).data
        assertEquals(2, successData.items.size)
    }

    @Test
    fun `invoke with UNKNOWN type should return empty page`() = runBlocking {
        // arrange
        val query = QueryState(type = MediaType.UNKNOWN)

        // act
        val result = sut.invoke(query)

        // assert
        coVerify(exactly = 0) { getter.getPage(any()) }
        assertTrue(result is UseCaseState.Success)
        assertTrue((result as UseCaseState.Success).data.items.isEmpty())
    }

    @Test
    fun `invoke should return Failure when getter fails`() = runBlocking {
        // arrange
        val query = QueryState(type = MediaType.MOVIE)
        val exception = Exception("Network error")
        coEvery { getter.getPage(any()) } throws exception

        // act
        val result = sut.invoke(query)

        // assert
        assertTrue(result is UseCaseState.Failure)
        val failure = result as UseCaseState.Failure
        assertEquals(exception, (failure.type as FailType.Exception).throwable)
    }
}
