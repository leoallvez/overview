package io.github.leoallvez.take.data.model

open class EntertainmentContentTest {


    companion object {

        internal fun makeContentJson(
            apiId: Long,
            fieldContentName: String,
            fieldContentValue: String,
            posterPath: String,
            voteAverage: Double
        ): String {
            return """{
            "id": $apiId,
            "$fieldContentName": "$fieldContentValue",
            "poster_path": "$posterPath",
            "vote_average": $voteAverage
        }""".trimMargin()
        }

        internal const val API_ID = 1L
        const val POSTER_PATH = "path/sample"
        const val VOTE_AVERAGE = 10.0

        //private val MOVIE_JSON = makeMovieJson(API_ID, TITLE, POSTER_PATH, VOTE_AVERAGE)
    }
}