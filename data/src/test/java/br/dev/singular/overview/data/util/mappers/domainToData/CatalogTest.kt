package br.dev.singular.overview.data.util.mappers.domainToData

import br.dev.singular.overview.domain.model.Catalog
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class CatalogTest {

    @Test
    fun `Catalog toData should map all fields correctly`() {
        // arrange
        val date = Date()
        val domainModel = Catalog(
            id = 1L,
            name = "Netflix",
            priority = 1,
            logoPath = "/logo.png",
            display = true,
            lastUpdate = date
        )

        // act
        val dataModel = domainModel.toData()

        // assert
        assertEquals(domainModel.id, dataModel.id)
        assertEquals(domainModel.name, dataModel.name)
        assertEquals(domainModel.priority, dataModel.priority)
        assertEquals(domainModel.logoPath, dataModel.logoPath)
        assertEquals(domainModel.display, dataModel.display)
        assertEquals(domainModel.lastUpdate, dataModel.lastUpdate)
    }
}
