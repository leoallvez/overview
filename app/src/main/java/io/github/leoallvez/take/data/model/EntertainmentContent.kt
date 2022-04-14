package io.github.leoallvez.take.data.model

import com.squareup.moshi.Json

interface EntertainmentContent {

    val apiId: Long

    @Json(name = "poster_path")
    val posterPath: String

    @Json(name = "vote_average")
    val voteAverage: Double

    fun getTitleDescription(): String
}