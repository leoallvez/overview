package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.CreditsDataModel
import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.MediaListDataModel
import br.dev.singular.overview.data.model.MovieDetailsDataModel
import br.dev.singular.overview.data.model.PersonDataModel
import br.dev.singular.overview.domain.model.MediaType
import org.junit.Assert.assertEquals
import org.junit.Test

class MovieDetailsTest {

    @Test
    fun `MovieDetailsDataModel toDomain should map all fields correctly`() {
        // arrange
        val dataModel = MovieDetailsDataModel(
            id = 1L,
            title = "Movie Title",
            originalTitle = "Original Title",
            releaseDate = "2020-01-01",
            runtime = 120,
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            overview = "Overview description",
            credits = CreditsDataModel(
                crew = listOf(
                    PersonDataModel(name = "Director 1", job = "Director"),
                    PersonDataModel(name = "Director 2", job = "Director"),
                    PersonDataModel(name = "Editor Name", job = "Editor")
                )
            ),
            videos = listOf(),
            catalogs = listOf(),
            similar = MediaListDataModel(
                results = listOf(
                    MediaDataModel(id = 2L, title = "Similar Movie")
                )
            )
        )

        // act
        val domainModel = dataModel.toDomain()

        // assert
        assertEquals(dataModel.id, domainModel.id)
        assertEquals(dataModel.betterTitle, domainModel.title)
        assertEquals(dataModel.releaseDate, domainModel.releaseDate)
        assertEquals(dataModel.runtime, domainModel.runtime)
        assertEquals(dataModel.posterPath, domainModel.posterPath)
        assertEquals(dataModel.backdropPath, domainModel.backdropPath)
        assertEquals(dataModel.overview, domainModel.overview)
        assertEquals("Director 1, Director 2", domainModel.directors.joinToString())
        assertEquals(1, domainModel.similar.size)
        assertEquals(2L, domainModel.similar[0].id)
        assertEquals(MediaType.MOVIE, domainModel.similar[0].type)
    }
}
