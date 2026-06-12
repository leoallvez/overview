package br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain

import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.presentation.createQueryUiStateMock
import org.junit.Assert.assertEquals
import org.junit.Test

class QueryUiStateTest {

    @Test
    fun `QueryUiState toDomain should map correctly`() {
        // arrange
        val ui = createQueryUiStateMock()

        // act
        val domain = ui.toDomain()

        // assert
        assertEquals(ui.key, domain.key)
        assertEquals(MediaType.MOVIE, domain.type)
        assertEquals(ui.isLiked, domain.isLiked)
        assertEquals(ui.query, domain.query)
        assertEquals(ui.page, domain.page)
    }
}
