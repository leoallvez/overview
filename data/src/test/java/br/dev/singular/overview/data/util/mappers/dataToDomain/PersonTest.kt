package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.PersonDataModel
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class PersonTest {

    @Test
    fun `PersonDataModel toDomain should map all fields correctly`() {
        // arrange
        val dataModel = PersonDataModel(
            id = 1L,
            name = "John Doe",
            profilePath = "/profile.jpg"
        )

        // act
        val domainModel = dataModel.toDomain()

        // assert
        domainModel.id shouldBeEqualTo dataModel.id
        domainModel.name shouldBeEqualTo dataModel.name
        domainModel.profilePath shouldBeEqualTo dataModel.profilePath
    }
}
