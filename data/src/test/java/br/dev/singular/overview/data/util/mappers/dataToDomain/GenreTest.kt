package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.GenreDataModel
import org.junit.Assert.assertEquals
import org.junit.Test

class GenreTest {

    @Test
    fun `GenreDataModel toDomain should map all fields correctly`() {
        // arrange
        val dataModel = GenreDataModel(id = 1L, name = "Action")

        // act
        val domainModel = dataModel.toDomain()

        // assert
        assertEquals(dataModel.id, domainModel.id)
        assertEquals(dataModel.name, domainModel.name)
    }
}
