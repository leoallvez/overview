package br.com.deepbyte.overview.data.model.media

import br.com.deepbyte.overview.data.model.person.PersonDetails

data class Credits(
    val cast: List<PersonDetails> = listOf(),
    val crew: List<PersonDetails> = listOf()
)
