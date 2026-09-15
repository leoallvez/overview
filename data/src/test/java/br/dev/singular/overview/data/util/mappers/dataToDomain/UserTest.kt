package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.UserDataModel
import br.dev.singular.overview.data.util.fakeUserDataModel
import org.junit.Assert.assertEquals
import org.junit.Test

class UserTest {

    @Test
    fun `UserDataModel toDomain should map all fields correctly`() {
        // arrange
        val dataModel = fakeUserDataModel

        // act
        val domainModel = dataModel.toDomain()

        // assert
        assertEquals(dataModel.id, domainModel.id)
        assertEquals(dataModel.name, domainModel.name)
        assertEquals(dataModel.email, domainModel.email)
        assertEquals(dataModel.photoUrl, domainModel.photoUrl)
    }

    @Test
    fun `UserDataModel toDomain should map null fields to empty strings`() {
        // arrange
        val dataModel = UserDataModel(id = "uid")

        // act
        val domainModel = dataModel.toDomain()

        // assert
        assertEquals("uid", domainModel.id)
        assertEquals("", domainModel.name)
        assertEquals("", domainModel.email)
        assertEquals("", domainModel.photoUrl)
    }
}
