package br.dev.singular.overview.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CreditsDataModel(
    val cast: List<PersonDataModel> = emptyList(),
    val crew: List<PersonDataModel> = emptyList()
)
