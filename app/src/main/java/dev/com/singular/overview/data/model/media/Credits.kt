package dev.com.singular.overview.data.model.media

import dev.com.singular.overview.data.model.person.Person

data class Credits(
    val cast: List<Person> = listOf(),
    val crew: List<Person> = listOf()
)
