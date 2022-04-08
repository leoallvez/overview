package io.github.leoallvez.take.util

import io.github.leoallvez.take.util.mock.MockPerson
import org.junit.Assert.assertEquals
import org.junit.Test

class ExtensionsTest {

    @Test
    fun `when call fromJson() in an valid json returns object`() {
        //Arrange
        val id = 1
        val firstName = "Job"
        val expected = "id:$id,first_name:$firstName"
        val validJson = """{"id":"$id","first_name":"$firstName"}"""
        //Act
        val model: MockPerson? = validJson.fromJson()
        //Assert
        assertEquals(expected, model?.toString())
    }

    @Test
    fun `when call fromJson() in an invalid json returns null`() {
        //Arrange
        val invalidJson = """{"id":1,!}"""
        val expected = null
        //Act
        val result: Any? = invalidJson.fromJson()
        //Assert
        assertEquals(expected, result)
    }

    @Test
    fun `when call fromJson() in an empty String returns null`() {
        //Arrange
        val emptyString = ""
        val expected = null
        //Act
        val result: Any? = emptyString.fromJson()
        //Assert
        assertEquals(expected, result)
    }
}
