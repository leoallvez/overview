package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.presentation.createGenreMock
import org.junit.Assert.assertEquals
import org.junit.Test

class GenreTest {

    @Test
    fun `Genre toUi should map correctly`() {
        // arrange
        val domain = createGenreMock()

        // act
        val ui = domain.toUi()

        // assert
        assertEquals(domain.id, ui.id)
        assertEquals(domain.name, ui.name)
    }

    @Test
    fun `list toUi should map list of Genre domains to list of GenreUiModels`() {
        val domains = listOf(
            createGenreMock().copy(id = 1L),
            createGenreMock().copy(id = 2L)
        )

        val uiModels = domains.toUi()

        assertEquals(2, uiModels.size)
        assertEquals(domains[0].id, uiModels[0].id)
        assertEquals(domains[1].id, uiModels[1].id)
    }
}
