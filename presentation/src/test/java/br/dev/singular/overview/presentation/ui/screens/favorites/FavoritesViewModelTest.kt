package br.dev.singular.overview.presentation.ui.screens.favorites

import br.dev.singular.overview.domain.usecase.media.IGetAllLocalMediasUseCase
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.ui.screens.common.UiEvent
import br.dev.singular.overview.presentation.ui.screens.common.UiEvents
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private val useCase: IGetAllLocalMediasUseCase = mockk(relaxed = true)
    private val uiEvents = UiEvents()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSetType should update queryState with new type`() = runTest {
        val sut = FavoritesViewModel(uiEvents, testDispatcher, useCase)
        val newType = MediaUiType.MOVIE

        sut.onSetType(newType)

        assertEquals(newType, sut.queryState.value.type)
    }

    @Test
    fun `ReloadFavorites event should trigger onReload`() = runTest {
        val sut = FavoritesViewModel(uiEvents, testDispatcher, useCase)
        val initialRefreshKey = sut.queryState.value.refreshKey

        uiEvents.trigger(UiEvent.ReloadFavorites)

        assertEquals(initialRefreshKey + 1, sut.queryState.value.refreshKey)
    }
}
