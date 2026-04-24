package br.dev.singular.overview.data.util.mappers.domainToData

import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.domain.model.Genre
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.MediaTypeGenres
import org.junit.Assert.assertEquals
import org.junit.Test

class MediaTypeGenresTest {

    @Test
    fun `MediaTypeGenres toData should map all genres to a list of MediaTypeGenreDataModel`() {
        // arrange
        val domainModel = MediaTypeGenres(
            type = MediaType.MOVIE,
            genres = listOf(
                Genre(id = 1L, name = "Action"),
                Genre(id = 2L, name = "Comedy")
            )
        )

        // act
        val dataList = domainModel.toData()

        // assert
        assertEquals(2, dataList.size)
        assertEquals(MediaDataType.MOVIE, dataList[0].type)
        assertEquals(1L, dataList[0].genreId)
        assertEquals(MediaDataType.MOVIE, dataList[1].type)
        assertEquals(2L, dataList[1].genreId)
    }
}
