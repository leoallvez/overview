package io.github.leoallvez.take.data.model

interface Audiovisual {
    val apiId: Long
    val posterPath: String
    val voteAverage: Double
    fun getContentTitle(): String
}
