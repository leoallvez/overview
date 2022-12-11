package br.com.deepbyte.overview.data.model.media

import br.com.deepbyte.overview.data.api.response.PersonDetails

data class Credits(
    val cast: List<PersonDetails> = listOf(),
    val crew: List<PersonDetails> = listOf()
)
