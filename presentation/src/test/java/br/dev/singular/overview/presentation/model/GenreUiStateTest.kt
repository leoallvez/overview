package br.dev.singular.overview.presentation.model

import br.dev.singular.overview.presentation.createGenreUiModelMock
import kotlinx.collections.immutable.toImmutableList
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GenreUiStateTest {

    private fun createState(count: Int, query: String = "") = GenreUiState(
        initial = null,
        selected = null,
        options = List(count) { index ->
            createGenreUiModelMock().copy(id = index.toLong(), name = "Genre $index")
        }.toImmutableList(),
        query = query
    )

    @Test
    fun `isSearchVisible should be true when there are many genres`() {
        // arrange
        val state = createState(count = 12)

        // act
        val result = state.isSearchVisible

        // assert
        assertTrue(result)
    }

    @Test
    fun `isSearchVisible should be false when there are few genres and no query`() {
        // arrange
        val state = createState(count = 11)

        // act
        val result = state.isSearchVisible

        // assert
        assertFalse(result)
    }

    @Test
    fun `isSearchVisible should be true when there is a query even with few genres`() {
        // arrange
        val state = createState(count = 1, query = "dra")

        // act
        val result = state.isSearchVisible

        // assert
        assertTrue(result)
    }

    @Test
    fun `isSearching should be false when query is blank`() {
        // arrange
        val state = createState(count = 5, query = "  ")

        // act
        val result = state.isSearching

        // assert
        assertFalse(result)
    }

    @Test
    fun `filteredOptions should return all options when query is blank`() {
        // arrange
        val state = createState(count = 5, query = "  ")

        // act
        val result = state.filteredOptions

        // assert
        assertEquals(state.options, result)
    }

    @Test
    fun `filteredOptions should match the trimmed query ignoring case`() {
        // arrange
        val options = listOf(
            createGenreUiModelMock().copy(id = 1L, name = "Action"),
            createGenreUiModelMock().copy(id = 2L, name = "Drama"),
            createGenreUiModelMock().copy(id = 3L, name = "Action & Adventure")
        ).toImmutableList()
        val state = GenreUiState(
            initial = null,
            selected = null,
            options = options,
            query = " ACTION "
        )

        // act
        val result = state.filteredOptions

        // assert
        assertEquals(listOf(1L, 3L), result.map { it.id })
    }
}
