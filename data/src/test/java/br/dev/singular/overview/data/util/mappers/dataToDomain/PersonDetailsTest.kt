package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.MediaCredits
import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.PersonDetailsDataModel
import br.dev.singular.overview.domain.model.MediaType
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class PersonDetailsTest {

    @Test
    fun `PersonDetailsDataModel toDomain should map all fields correctly including credits`() {
        // arrange
        val movieCredit = MediaDataModel(id = 101L, name = "Movie 1", type = MediaDataType.MOVIE)
        val tvShowCredit = MediaDataModel(id = 201L, name = "Show 1", type = MediaDataType.TV)

        val dataModel = PersonDetailsDataModel(
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
        domainModel.id shouldBeEqualTo dataModel.id
        domainModel.name shouldBeEqualTo dataModel.name
        domainModel.job shouldBeEqualTo dataModel.job
        domainModel.birthday shouldBeEqualTo dataModel.birthday
        domainModel.deathDay shouldBeEqualTo dataModel.deathDay
        domainModel.biography shouldBeEqualTo dataModel.biography
        domainModel.character shouldBeEqualTo dataModel.character
        domainModel.profilePath shouldBeEqualTo dataModel.profilePath
        domainModel.placeOfBirth shouldBeEqualTo dataModel.placeOfBirth

        // Credits check
        domainModel.movies.size shouldBeEqualTo 1
        domainModel.movies[0].id shouldBeEqualTo 101L
        domainModel.movies[0].type shouldBeEqualTo MediaType.MOVIE

        domainModel.tvShows.size shouldBeEqualTo 1
        domainModel.tvShows[0].id shouldBeEqualTo 201L
        domainModel.tvShows[0].type shouldBeEqualTo MediaType.TV
    }

    @Test
    fun `PersonDetailsDataModel toDomain should map empty credits to empty lists`() {
        // arrange
        val dataModel = PersonDetailsDataModel(
            id = 1L,
            tvShowsCredits = MediaCredits(list = emptyList()),
            moviesCredits = MediaCredits(list = emptyList())
        )

        // act
        val domainModel = dataModel.toDomain()

        // assert
        domainModel.tvShows.size shouldBeEqualTo 0
        domainModel.movies.size shouldBeEqualTo 0
    }
}
