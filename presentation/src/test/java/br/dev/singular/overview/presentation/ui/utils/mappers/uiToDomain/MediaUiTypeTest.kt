package br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain

import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.presentation.model.MediaUiType
import org.junit.Assert.assertEquals
import org.junit.Test

class MediaUiTypeTest {

    @Test
    fun `MediaUiType toDomain should map correctly`() {
        // assert
        assertEquals(MediaType.MOVIE, MediaUiType.MOVIE.toDomain())
        assertEquals(MediaType.TV, MediaUiType.TV.toDomain())
        assertEquals(MediaType.ALL, MediaUiType.ALL.toDomain())
    }
}
