package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.presentation.createVideoMock
import org.junit.Assert.assertEquals
import org.junit.Test

class VideoTest {

    @Test
    fun `Video toUi should map correctly`() {
        // arrange
        val domain = createVideoMock()

        // act
        val ui = domain.toUi()

        // assert
        assertEquals(domain.id, ui.id)
        assertEquals(domain.name, ui.name)
        assertEquals(domain.key, ui.key)
        assertEquals("https://img.youtube.com/vi/key/hqdefault.jpg", ui.thumbnailURL)
    }
}
