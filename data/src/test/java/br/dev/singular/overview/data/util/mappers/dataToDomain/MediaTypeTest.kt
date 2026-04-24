package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.domain.model.MediaType
import org.junit.Assert.assertEquals
import org.junit.Test

class MediaTypeTest {

    @Test
    fun `MediaDataType toDomain should map all types correctly`() {
        assertEquals(MediaType.MOVIE, MediaDataType.MOVIE.toDomain())
        assertEquals(MediaType.TV, MediaDataType.TV.toDomain())
        assertEquals(MediaType.ALL, MediaDataType.ALL.toDomain())
        assertEquals(MediaType.UNKNOWN, MediaDataType.UNKNOWN.toDomain())
    }
}
