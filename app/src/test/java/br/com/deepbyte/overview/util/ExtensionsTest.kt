package br.com.deepbyte.overview.util

import br.com.deepbyte.overview.util.mock.MockPerson
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldHaveSingleItem
import org.junit.Test

class ExtensionsTest {

    // fromJson method
    @Test
    fun `should create a model with same values when parse from json`() {
        // Arrange
        val modelExpected = MockPerson(1, "David")
        val validJson = """{"id":1,"first_name":"David"}"""
        // Act
        val modelResult: MockPerson? = validJson.fromJson()
        // Assert
        modelResult.shouldBeEqualTo(modelExpected)
    }

    @Test
    fun `should be null when try parse from an invalid json`() {
        // Arrange
        val invalidJson = """{"id":1,@ERROR}"""
        // Act
        val result: Any? = invalidJson.fromJson()
        // Assert
        result.shouldBeNull()
    }

    @Test
    fun `should be null when try parse from an empty string`() {
        // Arrange
        val emptyString = ""
        // Act
        val result: Any? = emptyString.fromJson()
        // Assert
        result.shouldBeNull()
    }

    // parseToList
    @Test
    fun `should have single item when parse to list a valid array json with one item`() {
        // Arrange
        val validJson = """[{"id":1,"first_name":"Ana"}]"""
        // Act
        val list = validJson.parseToList<MockPerson>()
        // Assert
        list.shouldHaveSingleItem()
    }

    @Test
    fun `should create a model with same values when parse from json array with one item`() {
        // Arrange
        val modelExpected = MockPerson(1, "Ana")
        val validJSON = """[{"id":1,"first_name":"Ana"}]"""
        // Act
        val list: List<MockPerson> = validJSON.parseToList()
        val modelResult = list.first()
        // Assert
        modelResult.shouldBeEqualTo(modelExpected)
    }

    @Test
    fun `should create an empty list when try parse an invalid json`() {
        // Arrange
        val invalidJSON = """[{"id":1,"first_name":"Ana"@ERROR}]"""
        // Act
        val list: List<MockPerson> = invalidJSON.parseToList()
        // Assert
        list.shouldBeEmpty()
    }

    @Test
    fun `should create an empty list when try parse an empty string`() {
        // Arrange
        val emptyString = ""
        // Act
        val list: List<MockPerson> = emptyString.parseToList()
        // Assert
        list.shouldBeEmpty()
    }
}
