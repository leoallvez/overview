package br.dev.singular.overview.presentation.ui.screens.favorites

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.repository.Page
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.media.IObserveFavoritesUseCase
import br.dev.singular.overview.presentation.createMediaMock
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.model.QueryUiState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {

    private val useCase: IObserveFavoritesUseCase = mockk()
    private val favorites = MutableSharedFlow<List<Media>>()
    private val testDispatcher = UnconfinedTestDispatcher()

    private val movie = createMediaMock().copy(id = 1L, type = MediaType.MOVIE)
    private val tvShow = createMediaMock().copy(id = 2L, type = MediaType.TV)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { useCase() } returns favorites
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createSut() = FavoritesViewModel(testDispatcher, useCase)

    @Test
    fun `onSetType should update queryState with new type`() = runTest {
        // arrange
        val sut = createSut()
        val newType = MediaUiType.MOVIE

        // act
        sut.onSetType(newType)

        // assert
        assertEquals(newType, sut.queryState.value.type)
    }

    @Test
    fun `first favorites emission should not trigger onReload`() = runTest {
        // arrange
        val sut = createSut()
        val initialRefreshKey = sut.queryState.value.refreshKey

        // act
        favorites.emit(listOf(movie))

        // assert
        assertEquals(initialRefreshKey, sut.queryState.value.refreshKey)
    }

    @Test
    fun `changed favorites should trigger onReload`() = runTest {
        // arrange
        val sut = createSut()
        favorites.emit(listOf(movie))
        val initialRefreshKey = sut.queryState.value.refreshKey

        // act
        favorites.emit(listOf(movie, tvShow))

        // assert
        assertEquals(initialRefreshKey + 1, sut.queryState.value.refreshKey)
    }

    @Test
    fun `unchanged favorites should not trigger onReload`() = runTest {
        // arrange
        val sut = createSut()
        favorites.emit(listOf(movie))
        val initialRefreshKey = sut.queryState.value.refreshKey

        // act
        favorites.emit(listOf(movie))

        // assert
        assertEquals(initialRefreshKey, sut.queryState.value.refreshKey)
    }

    @Test
    fun `onFetching should return every favorite in a single last page`() = runTest {
        // arrange
        val sut = createSut()
        favorites.emit(listOf(movie, tvShow))

        // act
        val result = sut.onFetching(QueryUiState(page = 1))

        // assert
        val expected = Page(listOf(movie, tvShow), currentPage = 1, isLastPage = true)
        assertEquals(UseCaseState.Success(expected), result)
    }

    @Test
    fun `onFetching should return only the favorites of the selected type`() = runTest {
        // arrange
        val sut = createSut()
        favorites.emit(listOf(movie, tvShow))

        // act
        val result = sut.onFetching(QueryUiState(page = 1, type = MediaUiType.TV))

        // assert
        val expected = Page(listOf(tvShow), currentPage = 1, isLastPage = true)
        assertEquals(UseCaseState.Success(expected), result)
    }

    @Test
    fun `onFetching should return an empty page when observing the favorites fails`() = runTest {
        // arrange
        every { useCase() } returns flow { throw IllegalStateException("error") }
        val sut = createSut()

        // act
        val result = sut.onFetching(QueryUiState(page = 1))

        // assert
        val expected = Page<Media>(emptyList(), currentPage = 1, isLastPage = true)
        assertEquals(UseCaseState.Success(expected), result)
    }
}
