package br.dev.singular.overview.data.util.mappers.domainToData

import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.domain.model.MediaType
import org.junit.Assert.assertEquals
import org.junit.Test

class MediaTypeTest {

    @Test
    fun `MediaType toData should map all types correctly`() {
        assertEquals(MediaDataType.MOVIE, MediaType.MOVIE.toData())
        assertEquals(MediaDataType.TV, MediaType.TV.toData())
        assertEquals(MediaDataType.ALL, MediaType.ALL.toData())
        assertEquals(MediaDataType.UNKNOWN, MediaType.UNKNOWN.toData())
    }
}
