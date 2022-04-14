package io.github.leoallvez.take.data.model

interface EntertainmentContent {

    val apiId: Long
    val posterPath: String
    val voteAverage: Double

    fun getTitleDescription(): String
}