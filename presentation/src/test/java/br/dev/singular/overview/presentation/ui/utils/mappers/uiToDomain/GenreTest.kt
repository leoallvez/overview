package br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain

import br.dev.singular.overview.presentation.createGenreUiModelMock
import org.junit.Assert.assertEquals
import org.junit.Test

class GenreTest {

    @Test
    fun `GenreUiModel toDomain should map correctly`() {
        // arrange
        val ui = createGenreUiModelMock()

        // act
        val domain = ui.toDomain()

        // assert
        assertEquals(ui.id, domain.id)
        assertEquals(ui.name, domain.name)
    }
}
