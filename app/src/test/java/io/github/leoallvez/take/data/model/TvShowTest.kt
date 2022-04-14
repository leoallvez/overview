package io.github.leoallvez.take.data.model

import io.github.leoallvez.take.util.fromJson
import org.junit.Assert.*

import org.junit.Test

class TvShowTest : EntertainmentContentTest()  {
    @Test
    fun `performs correct deserialization from a JSON to TVSHOW object`() {
        //Arrange
        val json = TV_SHOW_JSON
        //Act
        val model = json.fromJson<TvShow?>()
        //Assert
        assert(model is TvShow)
        assertEquals(TV_SHOW_JSON, model?.toJson())
    }

    private fun TvShow.toJson(): String = with(receiver = this) {
        return makeContentJson(
            apiId, fieldContentName = FIELD_CONTENT_NAME, name, posterPath, voteAverage
        )
    }

    companion object {
        private const val NAME = "MOVIE"
        private const val FIELD_CONTENT_NAME = "name"
        private val TV_SHOW_JSON = makeContentJson(
            API_ID, fieldContentName = FIELD_CONTENT_NAME, NAME, POSTER_PATH, VOTE_AVERAGE
        )
    }
}
