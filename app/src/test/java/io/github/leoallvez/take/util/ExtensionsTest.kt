package io.github.leoallvez.take.util

import br.com.deepbyte.take.util.fromJson
import br.com.deepbyte.take.util.parseToList
import io.github.leoallvez.take.util.mock.MockPerson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExtensionsTest {

    // fromJson method
    @Test fun fromJson_parserValidJson_objectHasValidProperties() {
        // Arrange
        val expectedId = 1
        val expectedName = "Job"
        val validJson = """{"id":"$expectedId","first_name":"$expectedName"}"""
        // Act
        val model: MockPerson? = validJson.fromJson()
        // Assert
        assertEquals(expectedId, model?.id)
        assertEquals(expectedName, model?.firstName)
    }

    @Test fun fromJson_parserValidJson_returnsAnObject() {
        // Arrange
        val id = 1
        val firstName = "Job"
        val expected = "id:$id,first_name:$firstName"
        val validJson = """{"id":"$id","first_name":"$firstName"}"""
        // Act
        val model: MockPerson? = validJson.fromJson()
        // Assert
        assertEquals(expected, model?.toString())
    }

    @Test fun fromJson_parserInvalidJson_returnsNull() {
        // Arrange
        val invalidJson = """{"id":1,@ERROR}"""
        val expected = null
        // Act
        val result: Any? = invalidJson.fromJson()
        // Assert
        assertEquals(expected, result)
    }

    @Test fun fromJson_parserEmptyString_returnsNull() {
        // Arrange
        val emptyString = ""
        val expected = null
        // Act
        val result: Any? = emptyString.fromJson()
        // Assert
        assertEquals(expected, result)
    }

    // parseToList
    @Test fun parseToList_parserJsonWithOneElement_listHasOneElement() {
        // Arrange
        val validJSON = """[{"id":1,"first_name":"Ana"}]"""
        val expected = 1
        // Act
        val list = validJSON.parseToList<MockPerson>()
        // Assert
        assertEquals(expected, list.size)
    }

    @Test fun parseToList_parserJsonWithOneElement_objectHasValidProperties() {
        // Arrange
        val expectedId = 1
        val expectedName = "Ana"
        val validJSON = """[{"id":$expectedId,"first_name":"$expectedName"}]"""
        // Act
        val list: List<MockPerson> = validJSON.parseToList()
        val model = list.first()
        // Assert
        assertEquals(expectedId, model.id)
        assertEquals(expectedName, model.firstName)
    }

    @Test fun parseToList_parserInvalidJson_listIsEmpty() {
        // Arrange
        val invalidJSON = """[{"id":1,"first_name":"Ana"@ERROR}]"""
        // Act
        val list: List<MockPerson> = invalidJSON.parseToList()
        // Assert
        assertTrue(list.isEmpty())
    }

    @Test fun parseToList_parserEmptyString_listIsEmpty() {
        // Arrange
        val emptyString = ""
        // Act
        val list: List<MockPerson> = emptyString.parseToList()
        // Assert
        assertTrue(list.isEmpty())
    }
}
