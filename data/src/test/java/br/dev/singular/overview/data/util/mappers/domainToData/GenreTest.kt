package br.dev.singular.overview.data.util.mappers.domainToData

import br.dev.singular.overview.domain.model.Genre
import org.junit.Assert.assertEquals
import org.junit.Test

class GenreTest {

    @Test
    fun `Genre toData should map all fields correctly`() {
        // arrange
        val domainModel = Genre(id = 2L, name = "Comedy")

        // act
        val dataModel = domainModel.toData()

        // assert
        assertEquals(domainModel.id, dataModel.id)
        assertEquals(domainModel.name, dataModel.name)
    }
}
