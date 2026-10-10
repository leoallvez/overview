package br.dev.singular.overview.data.util.mappers.domainToData

import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.PredictionInput
import org.junit.Assert.assertEquals
import org.junit.Test

class PredictionInputTest {

    @Test
    fun `PredictionInput toData should map all fields correctly`() {
        // arrange
        val domainModel = PredictionInput(
            id = 1L,
            type = MediaType.MOVIE,
            title = "Movie Title",
            genres = listOf("Comedy")
        )

        // act
        val dataModel = domainModel.toData()

        // assert
        assertEquals(1L, dataModel.id)
        assertEquals(MediaDataType.MOVIE, dataModel.type)
        assertEquals("Movie Title", dataModel.title)
        assertEquals(listOf("Comedy"), dataModel.genres)
    }
}
