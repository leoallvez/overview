package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.presentation.createPersonMock
import org.junit.Assert.assertEquals
import org.junit.Test

class PersonTest {

    @Test
    fun `Person toUi should map correctly`() {
        // arrange
        val domain = createPersonMock().copy(character = "Character / Extra")

        // act
        val ui = domain.toUi()

        // assert
        assertEquals(domain.id, ui.id)
        assertEquals(domain.name, ui.name)
        assertEquals(domain.job, ui.job)
        assertEquals("Character ", ui.character) // substringBefore('/') including the space
    }
}
