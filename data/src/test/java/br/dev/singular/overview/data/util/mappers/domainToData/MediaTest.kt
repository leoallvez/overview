package br.dev.singular.overview.data.util.mappers.domainToData

import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class MediaTest {

    @Test
    fun `Media toData should map all fields correctly`() {
        // arrange
        val date = Date()
        val domainModel = Media(
            id = 1L,
            type = MediaType.MOVIE,
            title = "Movie Title",
            posterPath = "/path.jpg",
            isLiked = true,
            lastUpdate = date
        )

        // act
        val dataModel = domainModel.toData()

        // assert
        assertEquals(domainModel.id, dataModel.id)
        assertEquals(MediaDataType.MOVIE, dataModel.type)
        assertEquals(domainModel.title, dataModel.title)
        assertEquals(domainModel.title, dataModel.name)
        assertEquals(domainModel.posterPath, dataModel.posterPath)
        assertEquals(domainModel.isLiked, dataModel.isLiked)
        assertEquals(domainModel.lastUpdate, dataModel.lastUpdate)
    }
}
