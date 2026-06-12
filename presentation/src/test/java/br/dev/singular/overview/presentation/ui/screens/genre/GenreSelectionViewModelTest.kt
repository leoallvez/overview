package br.dev.singular.overview.presentation.ui.screens.genre

import br.dev.singular.overview.domain.usecase.ICatalogQueryStateUseCase
import br.dev.singular.overview.domain.usecase.genre.IFetchGenresByTypeUseCase
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.createGenreMock
import br.dev.singular.overview.presentation.createGenreUiModelMock
import br.dev.singular.overview.presentation.createQueryStateMock
import br.dev.singular.overview.presentation.ui.screens.genre.interaction.GenreSelectionIntent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GenreSelectionViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val fetchGenresUseCase: IFetchGenresByTypeUseCase = mockk()
    private val queryStateUseCase: ICatalogQueryStateUseCase = mockk()

    private lateinit var sut: GenreSelectionViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        sut = GenreSelectionViewModel(
            dispatcher = testDispatcher,
            fetchGenresUseCase = fetchGenresUseCase,
            queryStateUseCase = queryStateUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Load intent should emit Success state when data is loaded successfully`() = runTest {
        // arrange
        val genres = listOf(createGenreMock())
        val queryState = createQueryStateMock().copy(genre = genres.first())
        coEvery { queryStateUseCase.get() } returns queryState
        coEvery { fetchGenresUseCase(any()) } returns genres

        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            sut.uiState.collect()
        }

        // act
        sut.handleIntent(GenreSelectionIntent.Load)
        advanceUntilIdle()

        // assert
        val currentState = sut.uiState.value
        assertTrue(currentState is UiState.Success)
        val successData = (currentState as UiState.Success).data
        assertEquals(1, successData.options.size)
        assertEquals(genres.first().id, successData.selectedId)

        job.cancel()
    }

    @Test
    fun `Load intent should emit Error state when an exception occurs`() = runTest {
        // arrange
        coEvery { queryStateUseCase.get() } throws Exception("Test exception")

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            sut.uiState.collect()
        }

        // act
        sut.handleIntent(GenreSelectionIntent.Load)
        advanceUntilIdle()

        // assert
        assertTrue(sut.uiState.value is UiState.Error)
    }

    @Test
    fun `Select intent should call queryStateUseCase save with updated genre`() = runTest {
        // arrange
        val initialGenre = createGenreMock()
        val queryState = createQueryStateMock().copy(genre = initialGenre)
        val newGenreUi = createGenreUiModelMock().copy(id = 2L, name = "Comedy")

        coEvery { queryStateUseCase.get() } returns queryState
        coEvery { fetchGenresUseCase(any()) } returns listOf(initialGenre)
        coEvery { queryStateUseCase.save(any()) } returns Unit

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            sut.uiState.collect()
        }

        // Load to set currentQuery in ViewModel
        sut.handleIntent(GenreSelectionIntent.Load)
        advanceUntilIdle()

        // act
        sut.handleIntent(GenreSelectionIntent.Select(newGenreUi))
        advanceUntilIdle()

        // assert
        coVerify {
            queryStateUseCase.save(match { it.genre?.id == newGenreUi.id })
        }
    }
}
