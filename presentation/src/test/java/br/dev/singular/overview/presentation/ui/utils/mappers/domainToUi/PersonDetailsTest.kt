package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.presentation.createPersonDetailsMock
import org.junit.Assert.assertEquals
import org.junit.Test

class PersonDetailsTest {

    @Test
    fun `Person toUi should map correctly`() {
        // arrange
        val domain = createPersonDetailsMock().copy(character = "Character / Extra")

        // act
        val ui = domain.toUi()

        // assert
        assertEquals(domain.id, ui.id)
        assertEquals(domain.name, ui.name)
        assertEquals(domain.job, ui.job)
        assertEquals("Character ", ui.character) // substringBefore('/') including the space
    }
}
