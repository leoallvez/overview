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
        //Assert
        assert(model is Movie)
        assertEquals(MOVIE_JSON, model?.toJson())
    }

    private fun Movie.toJson(): String = with(receiver = this) {
        return makeContentJson(
            apiId, FIELD_CONTENT_NAME, title, posterPath, voteAverage
        )
    }

    companion object {
        private const val TITLE = "MOVIE"
        private const val FIELD_CONTENT_NAME = "title"
        private val MOVIE_JSON = makeContentJson(
            API_ID, fieldContentName = FIELD_CONTENT_NAME, TITLE, POSTER_PATH, VOTE_AVERAGE
        )
    }
}
