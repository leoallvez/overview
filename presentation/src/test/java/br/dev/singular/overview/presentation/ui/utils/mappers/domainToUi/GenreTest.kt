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
}
