package br.dev.singular.overview.presentation.ui.screens.catalog.selection

import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.domain.usecase.ICatalogQueryStateUseCase
import br.dev.singular.overview.domain.usecase.ICatalogTooltipDismissedUseCase
import br.dev.singular.overview.domain.usecase.catalog.IGetAllCatalogUseCase
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.createCatalogMock
import br.dev.singular.overview.presentation.createCatalogUiModelMock
import br.dev.singular.overview.presentation.ui.screens.catalog.selection.interaction.CatalogSelectionIntent
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
class CatalogSelectionViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getAllUseCase: IGetAllCatalogUseCase = mockk()
    private val queryStateUseCase: ICatalogQueryStateUseCase = mockk(relaxed = true)
    private val catalogTooltipDismissedUseCase: ICatalogTooltipDismissedUseCase =
        mockk(relaxed = true)

    private lateinit var sut: CatalogSelectionViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        sut = CatalogSelectionViewModel(
            getAllUseCase = getAllUseCase,
            queryStateUseCase = queryStateUseCase,
            catalogTooltipDismissedUseCase = catalogTooltipDismissedUseCase,
            dispatcher = testDispatcher,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onLoad should emit Success state when data is loaded successfully`() = runTest {
        // arrange
        val catalogs = listOf(createCatalogMock())
        val queryState = QueryState(catalog = catalogs.first())
        coEvery { queryStateUseCase.get() } returns queryState
        coEvery { getAllUseCase() } returns catalogs
        coEvery { catalogTooltipDismissedUseCase.isDismissed() } returns false

        // Start collecting uiState in a background coroutine
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            sut.uiState.collect()
        }

        // act
        sut.handleIntent(CatalogSelectionIntent.Load)
        advanceUntilIdle()

        // assert
        val currentState = sut.uiState.value
        assertTrue(currentState is UiState.Success)
        assertEquals(1, (currentState as UiState.Success).data.options.size)
        assertEquals(false, sut.tooltipDismissed.value)

        job.cancel()
    }

    @Test
    fun `onLoad should emit Error state when catalogs list is empty`() = runTest {
        // arrange
        coEvery { queryStateUseCase.get() } returns null
        coEvery { getAllUseCase() } returns emptyList()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            sut.uiState.collect()
        }

        // act
        sut.handleIntent(CatalogSelectionIntent.Load)
        advanceUntilIdle()

        // assert
        assertTrue(sut.uiState.value is UiState.Error)
    }

    @Test
    fun `onSelect should call queryStateUseCase save`() = runTest {
        // arrange
        val catalogUi = createCatalogUiModelMock()

        // act
        sut.handleIntent(CatalogSelectionIntent.Select(catalogUi))
        advanceUntilIdle()

        // assert
        coVerify { queryStateUseCase.save(any()) }
    }

    @Test
    fun `onDismissTooltip should call dismiss and update state`() = runTest {
        // act
        sut.handleIntent(CatalogSelectionIntent.DismissTooltip)
        advanceUntilIdle()

        // assert
        coVerify { catalogTooltipDismissedUseCase.dismiss() }
        assertEquals(true, sut.tooltipDismissed.value)
    }
}
