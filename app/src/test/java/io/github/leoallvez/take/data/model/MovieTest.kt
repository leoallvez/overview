package io.github.leoallvez.take.data.model

import io.github.leoallvez.take.util.fromJson
import org.junit.Assert.assertEquals
import org.junit.Test

class MovieTest : EntertainmentContentTest() {

    @Test
    fun `performs correct deserialization from a JSON to MOVIE object`() {
        //Arrange
        val json = MOVIE_JSON
        //Act
        val model = json.fromJson<Movie?>()
        val jsonExpected = model?.toJson(FIELD_NAME)
        //Assert
        assert(model is Movie)
        assertEquals(MOVIE_JSON, jsonExpected)
    }

    companion object {
        private const val TITLE = "MOVIE"
        private const val FIELD_NAME = "title"
        private val MOVIE_JSON = makeContentJson(
            API_ID, fieldName = FIELD_NAME, TITLE, POSTER_PATH, VOTE_AVERAGE
        )
    }
}
