package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.presentation.createCatalogMock
import br.dev.singular.overview.presentation.createGenreMock
import br.dev.singular.overview.presentation.createQueryStateMock
import br.dev.singular.overview.presentation.model.MediaUiType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class QueryStateTest {

    @Test
    fun `QueryState toUi should map correctly`() {
        // arrange
        val domain = createQueryStateMock().copy(
            genre = createGenreMock(),
            catalog = createCatalogMock()
        )

        // act
        val ui = domain.toUi()

        // assert
        assertEquals(domain.key, ui.key)
        assertEquals(MediaUiType.MOVIE, ui.type)
        assertEquals(domain.isLiked, ui.isLiked)
        assertEquals(domain.query, ui.query)
        assertEquals(domain.page, ui.page)
        assertNotNull(ui.genre)
        assertEquals(domain.genre?.id, ui.genre?.id)
        assertNotNull(ui.catalog)
        assertEquals(domain.catalog?.id, ui.catalog?.id)
    }
}
