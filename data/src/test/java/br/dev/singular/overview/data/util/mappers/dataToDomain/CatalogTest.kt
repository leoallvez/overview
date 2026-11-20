package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.CatalogDataModel
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class CatalogTest {

    @Test
    fun `CatalogDataModel toDomain should map all fields correctly`() {
        // arrange
        val date = Date()
        val dataModel = CatalogDataModel(
            id = 1L,
            name = "Netflix",
            priority = 1,
            logoPath = "/logo.png",
            display = true,
            lastUpdate = date
        )

        // act
        val domainModel = dataModel.toDomain()

        // assert
        assertEquals(dataModel.id, domainModel.id)
        assertEquals(dataModel.name, domainModel.name)
        assertEquals(dataModel.priority, domainModel.priority)
        assertEquals(dataModel.logoPath, domainModel.logoPath)
        assertEquals(dataModel.display, domainModel.display)
        assertEquals(dataModel.lastUpdate, domainModel.lastUpdate)
    }
}
