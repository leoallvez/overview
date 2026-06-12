package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.presentation.createMediaMock
import br.dev.singular.overview.presentation.model.MediaUiType
import org.junit.Assert.assertEquals
import org.junit.Test

class MediaTest {

    @Test
    fun `Media toUi should map correctly`() {
        // arrange
        val domain = createMediaMock()

        // act
        val ui = domain.toUi()

        // assert
        assertEquals(domain.id, ui.id)
        assertEquals(domain.title, ui.title)
        assertEquals(domain.isLiked, ui.isLiked)
        assertEquals(MediaUiType.MOVIE, ui.type)
    }
}
