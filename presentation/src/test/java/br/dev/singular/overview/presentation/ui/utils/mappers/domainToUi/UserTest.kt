package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.presentation.createUserMock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class UserTest {

    @Test
    fun `User toUi should map correctly`() {
        // arrange
        val domain = createUserMock()

        // act
        val ui = domain.toUi()

        // assert
        assertEquals(domain.id, ui.id)
        assertEquals(domain.name, ui.name)
        assertEquals(domain.email, ui.email)
        assertEquals(domain.photoUrl, ui.photoURL)
        assertNull(ui.previewDrawableRes)
    }
}
