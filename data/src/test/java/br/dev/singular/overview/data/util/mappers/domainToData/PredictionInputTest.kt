package br.dev.singular.overview.data.util.mappers.domainToData

import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.PredictionFavoriteDataModel
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.PredictionInput
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

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

    @Test
    fun `PredictionInput toData should map the favorites and their genres`() {
        // arrange
        val domainModel = PredictionInput(
            id = 1L,
            type = MediaType.MOVIE,
            title = "Movie Title",
            genres = listOf("Comedy"),
            favorites = listOf(
                favorite(title = "Clube da Luta", originalTitle = "Fight Club"),
                favorite(title = "Dark", originalTitle = "", type = MediaType.TV)
            ),
            favoriteGenres = mapOf("Drama" to 2)
        )

        // act
        val dataModel = domainModel.toData()

        // assert
        val expected = listOf(
            PredictionFavoriteDataModel(title = "Fight Club", type = MediaDataType.MOVIE),
            PredictionFavoriteDataModel(title = "Dark", type = MediaDataType.TV)
        )
        assertEquals(expected, dataModel.favorites)
        assertEquals(mapOf("Drama" to 2), dataModel.favoriteGenres)
    }

    private fun favorite(
        title: String,
        originalTitle: String,
        type: MediaType = MediaType.MOVIE
    ) = Media(
        id = 2L,
        type = type,
        title = title,
        isLiked = true,
        posterPath = "",
        lastUpdate = Date(),
        originalTitle = originalTitle
    )
}
