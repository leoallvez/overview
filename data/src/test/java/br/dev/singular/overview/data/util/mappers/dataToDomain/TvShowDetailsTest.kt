package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.MediaListDataModel
import br.dev.singular.overview.data.model.TvShowDetailsDataModel
import br.dev.singular.overview.domain.model.MediaType
import org.junit.Assert.assertEquals
import org.junit.Test

class TvShowDetailsTest {

    @Test
    fun `TvShowDetailsDataModel toDomain should map all fields correctly`() {
        // arrange
        val dataModel = TvShowDetailsDataModel(
            id = 1L,
            name = "TV Show Name",
            originalName = "Original Name",
            numberOfSeasons = 5,
            numberOfEpisodes = 50,
            episodeRuntime = listOf(45),
            firstAirDate = "2020-01-01",
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            overview = "Overview description",
            videos = listOf(),
            catalogs = listOf(),
            similar = MediaListDataModel(
                results = listOf(
                    MediaDataModel(id = 2L, name = "Similar Show")
                )
            )
        )

        // act
        val domainModel = dataModel.toDomain()

        // assert
        assertEquals(dataModel.id, domainModel.id)
        assertEquals(dataModel.betterName, domainModel.name)
        assertEquals(dataModel.numberOfSeasons, domainModel.numberOfSeasons)
        assertEquals(dataModel.numberOfEpisodes, domainModel.numberOfEpisodes)
        assertEquals(dataModel.episodeRuntime, domainModel.episodeRuntime)
        assertEquals(dataModel.firstAirDate, domainModel.firstAirDate)
        assertEquals(dataModel.posterPath, domainModel.posterPath)
        assertEquals(dataModel.backdropPath, domainModel.backdropPath)
        assertEquals(dataModel.overview, domainModel.overview)
        assertEquals(1, domainModel.similar.size)
        assertEquals(2L, domainModel.similar[0].id)
        assertEquals(MediaType.TV, domainModel.similar[0].type)
    }
}
