package br.dev.singular.overview.domain.model

data class Person(
    val id: Long,
    val job: String,
    val name: String,
    val birthday: String,
    val deathDay: String,
    val biography: String,
    val character: String,
    val profilePath: String,
    val placeOfBirth: String,
    val tvShows: List<Media>,
    val movies: List<Media>
)
