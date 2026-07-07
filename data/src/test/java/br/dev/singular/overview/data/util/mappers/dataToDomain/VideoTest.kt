package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.VideoDataModel
import org.junit.Assert.assertEquals
import org.junit.Test

class VideoTest {

    @Test
    fun `VideoDataModel toDomain should map all fields correctly`() {
        // arrange
        val dataModel = VideoDataModel(
            id = "5c9294240e0a267cd516835f",
            key = "nEtH09HPrM",
            name = "Official Trailer"
        )

        // act
        val domainModel = dataModel.toDomain()

        // assert
        assertEquals(dataModel.id, domainModel.id)
        assertEquals(dataModel.key, domainModel.key)
        assertEquals(dataModel.name, domainModel.name)
    }
}
