package br.com.deepbyte.overview.data.model.media

import br.com.deepbyte.overview.data.model.person.Person

data class Credits(
    val cast: List<Person> = listOf(),
    val crew: List<Person> = listOf()
)
