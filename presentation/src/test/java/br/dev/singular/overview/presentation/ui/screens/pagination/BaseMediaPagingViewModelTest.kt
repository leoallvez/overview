package br.dev.singular.overview.presentation.ui.screens.pagination

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.repository.Page
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.presentation.model.QueryUiState
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseMediaPagingViewModelTest {

    private val fetcher: suspend (QueryUiState) -> UseCaseState<Page<Media>> = mockk()

    private class TestViewModel(
        private val fetcher: suspend (QueryUiState) -> UseCaseState<Page<Media>>
    ) : BaseMediaPagingViewModel() {
        override suspend fun onFetching(query: QueryUiState): UseCaseState<Page<Media>> {
            return fetcher(query)
        }
    }

    @Test
    fun `onQueryChanged should update queryState value`() {
        // arrange
        val sut = TestViewModel(fetcher)
        val newQuery = QueryUiState(query = "new search")

        // act
        sut.onQueryChanged(newQuery)

        // assert
        assertEquals(newQuery, sut.queryState.value)
    }

    @Test
    fun `onReload should update refreshKey in queryState`() {
        // arrange
        val sut = TestViewModel(fetcher)
        val initialRefreshKey = sut.queryState.value.refreshKey

        // act
        sut.onReload()

        // assert
        assertNotEquals(initialRefreshKey, sut.queryState.value.refreshKey)
        assertEquals(initialRefreshKey + 1, sut.queryState.value.refreshKey)
    }

    @Test
    fun `initial queryState should be default`() {
        // arrange
        val sut = TestViewModel(fetcher)

        // assert
        assertEquals(QueryUiState(), sut.queryState.value)
    }
}
