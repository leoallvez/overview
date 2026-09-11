package br.dev.singular.overview.presentation.ui.screens.catalog.details

import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.domain.usecase.ICatalogQueryStateUseCase
import br.dev.singular.overview.domain.usecase.media.IGetRemoteMediasUseCase
import br.dev.singular.overview.presentation.createCatalogMock
import br.dev.singular.overview.presentation.createCatalogUiModelMock
import br.dev.singular.overview.presentation.createGenreUiModelMock
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.model.ScrollUiState
import br.dev.singular.overview.presentation.ui.screens.catalog.details.interaction.CatalogDetailIntent
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getMediasUseCase: IGetRemoteMediasUseCase = mockk()
    private val queryStateUseCase: ICatalogQueryStateUseCase = mockk(relaxed = true)

    private val queryObserveFlow = MutableSharedFlow<QueryState?>(replay = 1)

    private lateinit var sut: CatalogDetailsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        every { queryStateUseCase.observe() } returns queryObserveFlow

        sut = CatalogDetailsViewModel(
            dispatcher = testDispatcher,
            getMediasUseCase = getMediasUseCase,
            queryStateUseCase = queryStateUseCase,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Observe query state should update query`() = runTest {
        // arrange
        val catalog = createCatalogMock()
        val queryState = QueryState(
            catalog = catalog,
            type = MediaType.MOVIE
        )

        // act
        queryObserveFlow.emit(queryState)
        advanceUntilIdle()

        // assert
        assertEquals(catalog.name, sut.queryState.value.catalog?.name)
    }

    @Test
    fun `SelectType intent should update query and save state`() = runTest {
        // arrange
        val type = MediaUiType.MOVIE

        // act
        sut.handleIntent(CatalogDetailIntent.SelectType(type))
        advanceUntilIdle()

        // assert
        assertEquals(type, sut.queryState.value.type)
        coVerify { queryStateUseCase.save(any()) }
    }

    @Test
    fun `SelectType intent should clear genre when type is selected`() = runTest {
        // arrange
        val initialGenre = createGenreUiModelMock()
        sut.handleIntent(CatalogDetailIntent.SelectGenre(initialGenre))

        val newType = MediaUiType.TV

        // act
        sut.handleIntent(CatalogDetailIntent.SelectType(newType))
        advanceUntilIdle()

        // assert
        assertEquals(newType, sut.queryState.value.type)
        assertEquals(null, sut.queryState.value.genre)
    }

    @Test
    fun `SelectGenre intent should update query and save state`() = runTest {
        // arrange
        val genre = createGenreUiModelMock()

        // act
        sut.handleIntent(CatalogDetailIntent.SelectGenre(genre))
        advanceUntilIdle()

        // assert
        assertEquals(genre, sut.queryState.value.genre)
        coVerify { queryStateUseCase.save(any()) }
    }

    @Test
    fun `SelectCatalog intent should update query and save state`() = runTest {
        // arrange
        val catalog = createCatalogUiModelMock()

        // act
        sut.handleIntent(CatalogDetailIntent.SelectCatalog(catalog))
        advanceUntilIdle()

        // assert
        assertEquals(catalog, sut.queryState.value.catalog)
        coVerify { queryStateUseCase.save(any()) }
    }

    @Test
    fun `ChangeScrollState intent should update scroll state`() = runTest {
        // arrange
        val scrollState = ScrollUiState(index = 10, offset = 100)

        // act
        sut.handleIntent(CatalogDetailIntent.ChangeScrollState(scrollState))

        // assert
        assertEquals(scrollState, sut.scrollState.value)
    }
}
