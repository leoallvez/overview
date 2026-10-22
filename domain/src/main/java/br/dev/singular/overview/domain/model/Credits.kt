package br.dev.singular.overview.domain.model

data class Credits(
    val cast: List<Person> = listOf(),
    val crew: List<Person> = listOf()
)
