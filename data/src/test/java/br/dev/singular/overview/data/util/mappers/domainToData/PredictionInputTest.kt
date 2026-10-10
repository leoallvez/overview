package br.dev.singular.overview.data.util.mappers.domainToData

import br.dev.singular.overview.domain.model.PredictionInput
import org.junit.Assert.assertEquals
import org.junit.Test

class PredictionInputTest {

    @Test
    fun `PredictionInput toData should map all fields correctly`() {
        // arrange
        val domainModel = PredictionInput(
            id = 1L,
            title = "Movie Title",
            genres = listOf("Comedy")
        )

        // act
        val dataModel = domainModel.toData()

        // assert
        assertEquals(1L, dataModel.id)
        assertEquals("Movie Title", dataModel.title)
        assertEquals(listOf("Comedy"), dataModel.genres)
    }
}
