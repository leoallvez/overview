package br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain

import br.dev.singular.overview.presentation.createCatalogUiModelMock
import org.junit.Assert.assertEquals
import org.junit.Test

class CatalogTest {

    @Test
    fun `CatalogUiModel toDomain should map correctly`() {
        // arrange
        val ui = createCatalogUiModelMock()

        // act
        val domain = ui.toDomain()

        // assert
        assertEquals(ui.id, domain.id)
        assertEquals(ui.name, domain.name)
        assertEquals(ui.priority, domain.priority)
    }
}
