package br.dev.singular.overview.data.util.mappers.domainToData

import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.Suggestion
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class SuggestionTest {

    @Test
    fun `Suggestion toData should map all fields correctly`() {
        // arrange
        val date = Date()
        val domainModel = Suggestion(
            id = 1L,
            key = "trending/movie/week",
            order = 5,
            type = MediaType.MOVIE,
            isActive = true,
            lastUpdate = date
        )

        // act
        val dataModel = domainModel.toData()

        // assert
        assertEquals(domainModel.id, dataModel.id)
        assertEquals(domainModel.order, dataModel.order)
        assertEquals(MediaDataType.MOVIE, dataModel.type)
        assertEquals(domainModel.key, dataModel.sourceKey)
        assertEquals(domainModel.isActive, dataModel.isActive)
        assertEquals(domainModel.lastUpdate, dataModel.lastUpdate)
    }
}
