package br.dev.singular.overview.domain.model

data class Person(
    val id: Long,
    val name: String,
    val order: Int,
    val job: String,
    val character: String,
    val profilePath: String
)
