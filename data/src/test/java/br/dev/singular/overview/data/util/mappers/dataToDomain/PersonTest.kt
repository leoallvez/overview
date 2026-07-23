package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.MediaCredits
import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.PersonDataModel
import br.dev.singular.overview.domain.model.MediaType
import org.junit.Assert.assertEquals
import org.junit.Test

class PersonTest {

    @Test
    fun `PersonDataModel toDomain should map all fields correctly including credits`() {
        // arrange
        val movieCredit = MediaDataModel(id = 101L, name = "Movie 1", type = MediaDataType.MOVIE)
        val tvShowCredit = MediaDataModel(id = 201L, name = "Show 1", type = MediaDataType.TV)

        val dataModel = PersonDataModel(
            id = 1L,
            name = "John Doe",
            job = "Actor",
            birthday = "1990-01-01",
            deathDay = "2020-01-01",
            biography = "A famous actor.",
            character = "Hero",
            profilePath = "/profile.jpg",
            placeOfBirth = "New York",
            tvShowsCredits = MediaCredits(list = listOf(tvShowCredit)),
            moviesCredits = MediaCredits(list = listOf(movieCredit))
        )

        // act
        val domainModel = dataModel.toDomain()

        // assert
        assertEquals(dataModel.id, domainModel.id)
        assertEquals(dataModel.name, domainModel.name)
        assertEquals(dataModel.job, domainModel.job)
        assertEquals(dataModel.birthday, domainModel.birthday)
        assertEquals(dataModel.deathDay, domainModel.deathDay)
        assertEquals(dataModel.biography, domainModel.biography)
        assertEquals(dataModel.character, domainModel.character)
        assertEquals(dataModel.profilePath, domainModel.profilePath)
        assertEquals(dataModel.placeOfBirth, domainModel.placeOfBirth)

        // Credits check
        assertEquals(1, domainModel.movies.size)
        assertEquals(101L, domainModel.movies[0].id)
        assertEquals(MediaType.MOVIE, domainModel.movies[0].type)

        assertEquals(1, domainModel.tvShows.size)
        assertEquals(201L, domainModel.tvShows[0].id)
        assertEquals(MediaType.TV, domainModel.tvShows[0].type)
    }
}
