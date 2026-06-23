package br.dev.singular.overview.presentation.ui.screens.search

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.domain.repository.Page
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.media.IGetRemoteMediasUseCase
import br.dev.singular.overview.domain.usecase.suggestion.IGetAllSuggestionsUseCase
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.model.QueryUiState
import br.dev.singular.overview.presentation.ui.screens.search.interaction.SearchIntent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val mediasUseCase: IGetRemoteMediasUseCase = mockk()
    private val suggestionsUseCase: IGetAllSuggestionsUseCase = mockk()

    private lateinit var sut: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        sut = SearchViewModel(
            mediasUseCase = mediasUseCase,
            suggestionsUseCase = suggestionsUseCase,
            dispatcher = testDispatcher,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Loading for suggestions`() {
        assertTrue(sut.suggestionsState.value is UiState.Loading)
    }

    @Test
    fun `handleIntent Search should update queryState`() = runTest {
        // act
        sut.handleIntent(SearchIntent.Search("batman"))

        // assert
        assertEquals("batman", sut.queryState.value.query)
    }

    @Test
    fun `handleIntent SetType should update queryState`() = runTest {
        // act
        sut.handleIntent(SearchIntent.SetType(MediaUiType.TV))

        // assert
        assertEquals(MediaUiType.TV, sut.queryState.value.type)
    }

    @Test
    fun `handleIntent LoadSuggestions should update suggestionsState to Success`() = runTest {
        // arrange
        val suggestions = listOf(
            Suggestion(
                id = 1,
                order = 1,
                key = "Trending",
                type = MediaType.MOVIE,
                isActive = true,
                medias = listOf(mockk<Media>(relaxed = true)),
                lastUpdate = Date()
            )
        )
        coEvery { suggestionsUseCase.invoke() } returns UseCaseState.Success(suggestions)

        // act
        sut.handleIntent(SearchIntent.LoadSuggestions)
        advanceUntilIdle()

        // assert
        val state = sut.suggestionsState.value
        assertTrue(state is UiState.Success)
        assertEquals(1, (state as UiState.Success).data.size)
        assertTrue(state.data.containsKey("Trending"))
    }

    @Test
    fun `handleIntent LoadSuggestions should update suggestionsState to Error on failure`() = runTest {
        // arrange
        coEvery { suggestionsUseCase.invoke() } returns UseCaseState.Failure(FailType.NothingFound)

        // act
        sut.handleIntent(SearchIntent.LoadSuggestions)
        advanceUntilIdle()

        // assert
        assertTrue(sut.suggestionsState.value is UiState.Error)
    }

    @Test
    fun `onFetching should call mediasUseCase with mapped query`() = runTest {
        // arrange
        val queryUi = QueryUiState(query = "hero", type = MediaUiType.MOVIE)
        val expectedPage = Page<Media>(items = emptyList(), currentPage = 1, isLastPage = false)
        coEvery { mediasUseCase.invoke(any()) } returns UseCaseState.Success(expectedPage)

        // act
        val result = sut.onFetching(queryUi)

        // assert
        assertTrue(result is UseCaseState.Success)
        assertEquals(expectedPage, (result as UseCaseState.Success).data)
        coVerify {
            mediasUseCase.invoke(match {
                it.query == "hero" && it.type == MediaType.MOVIE
            })
        }
    }
}
