package br.dev.singular.overview.presentation.model

import br.dev.singular.overview.presentation.createCatalogUiModelMock
import kotlinx.collections.immutable.toImmutableList
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogUiStateTest {

    private fun createCatalogs(count: Int) = List(count) { index ->
        createCatalogUiModelMock().copy(id = index.toLong(), name = "Catalog $index")
    }.toImmutableList()

    private fun createState(count: Int, query: String = "") = CatalogUiState(
        initial = null,
        selected = null,
        options = createCatalogs(count),
        query = query
    )

    @Test
    fun `isSearchVisible should be true when there are many catalogs`() {
        // arrange
        val state = createState(count = 12)

        // act
        val result = state.isSearchVisible

        // assert
        assertTrue(result)
    }

    @Test
    fun `isSearchVisible should be false when there are few catalogs and no query`() {
        // arrange
        val state = createState(count = 11)

        // act
        val result = state.isSearchVisible

        // assert
        assertFalse(result)
    }

    @Test
    fun `isSearchVisible should be true when there is a query even with few catalogs`() {
        // arrange
        val state = createState(count = 1, query = "net")

        // act
        val result = state.isSearchVisible

        // assert
        assertTrue(result)
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
            createCatalogUiModelMock().copy(id = 1L, name = "Netflix"),
            createCatalogUiModelMock().copy(id = 2L, name = "Disney Plus"),
            createCatalogUiModelMock().copy(id = 3L, name = "Apple TV Plus")
        ).toImmutableList()
        val state = CatalogUiState(
            initial = null,
            selected = null,
            options = options,
            query = " PLUS "
        )

        // act
        val result = state.filteredOptions

        // assert
        assertEquals(listOf(2L, 3L), result.map { it.id })
    }
}
