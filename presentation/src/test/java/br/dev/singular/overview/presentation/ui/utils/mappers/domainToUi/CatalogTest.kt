package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.presentation.createCatalogMock
import org.junit.Assert.assertEquals
import org.junit.Test

class CatalogTest {

    @Test
    fun `Catalog toUi should map correctly`() {
        // arrange
        val domain = createCatalogMock()

        // act
        val ui = domain.toUi()

        // assert
        assertEquals(domain.id, ui.id)
        assertEquals(domain.name, ui.name)
        assertEquals(domain.priority, ui.priority)
    }
}
