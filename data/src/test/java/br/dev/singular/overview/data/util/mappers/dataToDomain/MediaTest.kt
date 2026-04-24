package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.MediaDataPage
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.domain.model.MediaType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class MediaTest {

    @Test
    fun `MediaDataModel toDomain should map all fields correctly`() {
        // arrange
        val date = Date()
        val dataModel = MediaDataModel(
            id = 1L,
            name = "Movie Name",
            title = "",
            posterPath = "/path.jpg",
            type = MediaDataType.MOVIE,
            isLiked = true,
            lastUpdate = date
        )

        // act
        val domainModel = dataModel.toDomain()

        // assert
        assertEquals(dataModel.id, domainModel.id)
        assertEquals(MediaType.MOVIE, domainModel.type)
        assertEquals("Movie Name", domainModel.title)
        assertEquals(dataModel.posterPath, domainModel.posterPath)
        assertEquals(dataModel.isLiked, domainModel.isLiked)
        assertEquals(dataModel.lastUpdate, domainModel.lastUpdate)
    }

    @Test
    fun `MediaDataModel toDomain should use title if name is empty`() {
        // arrange
        val dataModel = MediaDataModel(
            id = 1L,
            name = "",
            title = "Movie Title"
        )

        // act
        val domainModel = dataModel.toDomain()

        // assert
        assertEquals("Movie Title", domainModel.title)
    }

    @Test
    fun `MediaDataPage toDomain should map all fields correctly`() {
        // arrange
        val items = listOf(
            MediaDataModel(id = 1L, name = "Media 1"),
            MediaDataModel(id = 2L, name = "Media 2")
        )
        val page = MediaDataPage(
            page = 1,
            items = items,
            isLastPage = false
        )

        // act
        val domainPage = page.toDomain()

        // assert
        assertEquals(page.page, domainPage.currentPage)
        assertEquals(page.isLastPage, domainPage.isLastPage)
        assertEquals(2, domainPage.items.size)
        assertEquals(items[0].id, domainPage.items[0].id)
        assertEquals(items[1].id, domainPage.items[1].id)
    }
}
