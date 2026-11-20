package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.SuggestionDataModel
import br.dev.singular.overview.domain.model.MediaType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class SuggestionTest {

    @Test
    fun `SuggestionDataModel toDomain should map all fields correctly`() {
        // arrange
        val date = Date()
        val dataModel = SuggestionDataModel(
            id = 1L,
            order = 5,
            type = MediaDataType.MOVIE,
            sourceKey = "trending/movie/week",
            isActive = true,
            lastUpdate = date
        )

        // act
        val domainModel = dataModel.toDomain()

        // assert
        assertEquals(dataModel.id, domainModel.id)
        assertEquals(dataModel.order, domainModel.order)
        assertEquals(MediaType.MOVIE, domainModel.type)
        assertEquals(dataModel.sourceKey, domainModel.key)
        assertEquals(dataModel.isActive, domainModel.isActive)
        assertEquals(dataModel.lastUpdate, domainModel.lastUpdate)
    }
}
