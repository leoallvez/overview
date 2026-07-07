package br.dev.singular.overview.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PersonDataModel(
    val id: Long = 0,
    val name: String = "",
    val order: Int = 0,
    val job: String = "",
    val character: String = "",
    val profilePath: String = ""
) {
    val formattedCharacterName: String
        get() = character.substringBefore('/').trim()
}
