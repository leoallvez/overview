package io.github.leoallvez.take.data.model

open class EntertainmentContentTest {

    companion object {
        internal fun makeContentJson(
            apiId: Long,
            fieldName: String,
            fieldValue: String,
            posterPath: String,
            voteAverage: Double
        ): String {
            return """{
                "id": $apiId,
                "$fieldName": "$fieldValue",
                "poster_path": "$posterPath",
                "vote_average": $voteAverage
            }"""
                .replace(oldValue = "\n", newValue = "")
                .filter { !it.isWhitespace() }
        }

        internal const val API_ID = 1L
        const val POSTER_PATH = "path/sample"
        const val VOTE_AVERAGE = 10.0
    }
}
