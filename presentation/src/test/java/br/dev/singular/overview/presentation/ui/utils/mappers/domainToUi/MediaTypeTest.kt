package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.presentation.model.MediaUiType
import org.junit.Assert.assertEquals
import org.junit.Test

class MediaTypeTest {

    @Test
    fun `MediaType toUi should map correctly`() {
        // assert
        assertEquals(MediaUiType.MOVIE, MediaType.MOVIE.toUi())
        assertEquals(MediaUiType.TV, MediaType.TV.toUi())
        assertEquals(MediaUiType.ALL, MediaType.ALL.toUi())
        assertEquals(MediaUiType.ALL, MediaType.UNKNOWN.toUi())
    }
}
